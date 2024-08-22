package kolya.study.bookservice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table
public class Rating {
    @Id
    private Long bookId;
    private int rating;
}
