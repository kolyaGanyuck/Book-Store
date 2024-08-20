package kolya.study.bookservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
    private final BookRepository bookRepository;
    private final PdfConverter pdfConverter;
    private final ImageService imageService;
    private final TextService textService;
    @Value("${file.upload-dir}")
    private final String path = null;

    @Autowired
    public Controller(BookRepository bookRepository, PdfConverter pdfConverter, ImageService imageService, TextService textService) {
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
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @ModelAttribute Book book) {
        pdfConverter.convertPdfToText(file);
        imageService.getImageCoverFromPdf(file, book);
        bookRepository.save(book);
        return ResponseEntity.ok("Файл завантажено успішно");
    }

    @GetMapping("/catalog")
    public String getCatalog(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "catalog";
    }

    @GetMapping("/book/{id}")
    public String bookByTitle(@PathVariable Long id, Model model) {
        Optional<Book> book = bookRepository.findById(id);
        model.addAttribute("book", book.orElse(null));
        return "book";
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

