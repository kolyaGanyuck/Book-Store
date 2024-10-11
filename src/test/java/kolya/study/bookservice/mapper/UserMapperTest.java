package kolya.study.bookservice.mapper;

import kolya.study.bookservice.dto.UserDto;
import kolya.study.bookservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

class UserMapperTest {
    @Mock
    UserMapper userMapper;
    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }
    @Test
    void mapUserToUserDto() {
        User user = new User(1L, "user9", "password", "email@gmail.com", "activation-code-123", false, "test-image.jpg", new ArrayList<>());

        // Act
        UserDto userDto = userMapper.map(user);

        // Assert
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getProfileImage(), userDto.getProfileImage());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.isActivated(), userDto.isActive());
        assertEquals(user.getActivationCode(), userDto.getActivationCode());
    }
}