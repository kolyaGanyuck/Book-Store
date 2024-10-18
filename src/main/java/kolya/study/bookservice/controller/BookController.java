package kolya.study.bookservice.controller;

import jakarta.validation.Valid;
import kolya.study.bookservice.dto.UserDto;
import kolya.study.bookservice.entity.FavoriteBook;
import kolya.study.bookservice.entity.Genre;
import kolya.study.bookservice.exception.ImageProcessingException;
import kolya.study.bookservice.exception.PdfConversionException;
import kolya.study.bookservice.service.*;
import kolya.study.bookservice.entity.Book;
import kolya.study.bookservice.entity.Rating;
import kolya.study.bookservice.repository.BookRepository;
import kolya.study.bookservice.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mbeans.SparseUserDatabaseMBean;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@org.springframework.stereotype.Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final PdfConverter pdfConverter;
    private final ImageService imageService;
    private final TextService textService;
    private final MessageSource messageSource;
    private final UserService userService;
    FavoriteBook favoriteBook;

    @Value("${file.upload-dir}")
    private String path;


    @GetMapping("/")
    public String redirect() {
        return "redirect:/books/catalogue";
    }

    @GetMapping("/add-book")
    public String form(Model model) {
        model.addAttribute("genres", Genre.values());
        return "create-book";

    }


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @Valid @ModelAttribute Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book_payload", book);
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream().map(ObjectError::getDefaultMessage).toList());
            model.addAttribute("genres", Genre.values());
            model.addAttribute("userDto", userService.checkAuthentication());

            return "create-book";
        }
        try {
            pdfConverter.convertPdfToText(file);
            imageService.getImageCoverFromPdf(file, book);
            bookRepository.save(book);
        } catch (PdfConversionException | ImageProcessingException exception) {
            model.addAttribute("error", exception.getMessage());
            return "errors/error";

        }

        return "redirect:/books/catalogue";
    }

    @GetMapping("/catalogue")
    public String getCatalog(Model model, Principal principal) {
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("userDto", userService.checkAuthentication());
        model.addAttribute("genres", Genre.values());
        LoggerFactory.getLogger(BookController.class).info("Principal: {}", principal);

        return "catalogue";
    }

    @GetMapping("/book/{id}")
    public String bookByTitle(@PathVariable Long id, Model model) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Optional<Rating> ratingObj = ratingRepository.findByBookId(id);
            model.addAttribute("rating", ratingObj.map(Rating::getRating).orElse(0));
            model.addAttribute("book", book.get());
            model.addAttribute("userDto", userService.checkAuthentication());

            return "book";
        } else {
            model.addAttribute("error", "errors.book.not_found");
            return "/errors/error";
        }
    }

    @GetMapping("/read/{id}")
    public String readBook(@PathVariable Long id, @RequestParam(defaultValue = "1") int page, Model model, Locale locale) {
        try {
            Book book = bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("errors.book.not_found"));
            List<String> pages = textService.paginateText(Path.of(path + "\\ " + book.getTitle() + ".txt"));

            if (page < 1) page = 1;
            if (page > pages.size()) page = pages.size();

            model.addAttribute("bookTitle", bookRepository.findById(id).get().getTitle());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pages.size());
            model.addAttribute("bookId", id);
            model.addAttribute("currentPageText", pages.get(page - 1));
            model.addAttribute("userDto", userService.checkAuthentication());

            return "read-book";
        } catch (IOException | NoSuchElementException exception) {
            model.addAttribute("error", messageSource.getMessage(exception.getMessage(),
                    new Object[0], exception.getMessage(), locale));
            return "/errors/error";
        }
    }

    @GetMapping("/search/")
    public String findBooks(@RequestParam(name = "search") String title, Model model) {
        model.addAttribute("books", bookRepository.findAllByTitleForDisplayContainingIgnoreCase(title));
        model.addAttribute("genres", Genre.values());
        model.addAttribute("userDto", userService.checkAuthentication());

        log.info(title);
        return "catalogue";
    }

    @PostMapping("/books-by-genre")
    public String booksByGenre(@RequestParam String genre, Model model){
        List<Book> allByGenre = bookRepository.findAllByGenre(genre);
        model.addAttribute("genres", Genre.values());
        model.addAttribute("books", allByGenre);
        model.addAttribute("userDto", userService.checkAuthentication());

        log.info(genre);
        return "catalogue";
    }

}

