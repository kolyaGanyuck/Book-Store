package kolya.study.bookservice.entity;

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
    private Integer rating;
}
