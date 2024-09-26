package kolya.study.bookservice.dto;

import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@Component
public class UserDto {
    private Long id;
    private String username;
    private String profileImage;

    public UserDto(Long id, String username, String profileImage) {
        this.id = id;
        this.username = username;
        this.profileImage = profileImage;
    }
}
