package kolya.study.bookservice.mapper;

import kolya.study.bookservice.dto.UserDto;
import kolya.study.bookservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Map<User, UserDto> {
    @Override
    public UserDto map(User user) {
        return new UserDto(user.getId(),
                user.getUsername()
                , user.getProfileImage());
    }
}
