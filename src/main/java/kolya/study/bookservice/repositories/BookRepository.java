package kolya.study.bookservice.repositories;

import kolya.study.bookservice.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    List<Book> findAllByTitleIgnoreCase(String title);
    List<Book> findAllByTitleContainingIgnoreCase(String title);

}
