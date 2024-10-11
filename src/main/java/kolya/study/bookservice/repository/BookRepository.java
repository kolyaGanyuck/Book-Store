package kolya.study.bookservice.repository;

import kolya.study.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByTitleForDisplayContainingIgnoreCase(String title);

    List<Book> findAllByGenre(String genre);
}
