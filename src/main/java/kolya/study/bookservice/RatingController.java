package kolya.study.bookservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class RatingController {
    private final RatingRepository ratingRepository;
    private final RatingService ratingService;
    @Autowired
    public RatingController(RatingRepository ratingRepository, RatingService ratingService) {
        this.ratingRepository = ratingRepository;
        this.ratingService = ratingService;
    }

    @GetMapping("/rating/{value}/{bookId}")
    public String setRating(@PathVariable Integer value, @PathVariable Long bookId) {
        ratingService.setRating(value, bookId);
        return "redirect:/books/book/{bookId}";
    }
}
