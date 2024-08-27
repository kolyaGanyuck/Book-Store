package kolya.study.bookservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 3, max = 120, message = "{book.create.errors.title.size_is_invalid}")
    private String title;
    @Size(min = 1, max = 50, message = "{book.create.errors.genre.size_is_invalid}")
    private String genre;
    private String author;
    private int year;
    private String imageCover;

}
