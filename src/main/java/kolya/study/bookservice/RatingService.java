package kolya.study.bookservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public void setRating(Integer value, Long bookId) {
        Optional<Rating> rating = ratingRepository.findByBookId(bookId);
        if (rating.isEmpty()) {
            Rating newRating = new Rating();
            newRating.setRating(value);
            newRating.setBookId(bookId);
            ratingRepository.save(newRating);
            log.info("Рейтинг додано вперше");
        }
        else {
            Rating rating1 = rating.get();
            int avg =  Math.round((float) (rating1.getRating() + value) / 2);
            rating1.setRating(avg);
            ratingRepository.save(rating1);
            log.info("Рейтинг додано успішно");

        }
    }

}
