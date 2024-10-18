package kolya.study.bookservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
public class FavoriteBook {
    private Long bookId;
    private Long userId;

}
