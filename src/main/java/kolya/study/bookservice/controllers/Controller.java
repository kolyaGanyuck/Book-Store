package kolya.study.bookservice.controllers;

import kolya.study.bookservice.exceptions.ImageProcessingException;
import kolya.study.bookservice.exceptions.PdfConversionException;
import kolya.study.bookservice.services.ImageService;
import kolya.study.bookservice.services.PdfConverter;
import kolya.study.bookservice.services.TextService;
import kolya.study.bookservice.entities.Book;
import kolya.study.bookservice.entities.Rating;
import kolya.study.bookservice.repositories.BookRepository;
import kolya.study.bookservice.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller
@RequestMapping("/books")
public class Controller {
    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final PdfConverter pdfConverter;
    private final ImageService imageService;
    private final TextService textService;
    @Value("${file.upload-dir}")
    private final String path = null;

    @Autowired
    public Controller(RatingRepository ratingRepository, BookRepository bookRepository, PdfConverter pdfConverter, ImageService imageService, TextService textService) {
        this.ratingRepository = ratingRepository;
        this.bookRepository = bookRepository;
        this.pdfConverter = pdfConverter;
        this.imageService = imageService;
        this.textService = textService;
    }

    @GetMapping("/add-book")
    public String form() {
        return "create-book";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @ModelAttribute Book book, Model model) {
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
    public String getCatalog(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "catalogue";
    }

    @GetMapping("/book/{id}")
    public String bookByTitle(@PathVariable Long id, Model model) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Optional<Rating> ratingObj = ratingRepository.findByBookId(id);
            model.addAttribute("rating", ratingObj.map(Rating::getRating).orElse(0));
            model.addAttribute("book", book.get());
            return "book";
        } else {
            model.addAttribute("error", "Книга не знайдена");
            return "/errors/error";
        }
    }

    @GetMapping("/read/{id}")
    public String readBook(@PathVariable Long id, @RequestParam(defaultValue = "1") int page, Model model) throws IOException {
        List<String> pages = textService.paginateText(Path.of(path + "\\ " + bookRepository.findById(id).get().getTitle() + ".txt"));

        if (page < 1) page = 1;
        if (page > pages.size()) page = pages.size();

        model.addAttribute("bookTitle", bookRepository.findById(id).get().getTitle());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.size());
        model.addAttribute("bookId", id);
        model.addAttribute("currentPageText", pages.get(page - 1));

        return "readBook";
    }



}

