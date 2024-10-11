package kolya.study.bookservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import kolya.study.bookservice.dto.UserDto;
import kolya.study.bookservice.entity.User;
import kolya.study.bookservice.mapper.UserMapper;
import kolya.study.bookservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserImageService userImageService;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private UserMapper userMapper;

    private User user;
    private MultipartFile file;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "user9", "password", "email@gmail.com", "34574632", true, "test-image.jpg", new ArrayList<>());
        file = mock(MultipartFile.class);
    }

    @Test
    public void testCreateUser_UserExists() throws IOException {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.createUser(user, file);
        assertFalse(result.isPresent());
    }

    @Test
    public void testCreateUser_FileNotEmptyAndImageDoesNotExist() throws IOException {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("image.png");
        when(Files.exists(Path.of(System.getProperty("user.dir") + "/uploads/images/image.png"))).thenReturn(false);

        userService.createUser(user, file);

        verify(userImageService).saveImage(file);
        verify(userRepository).save(user);
        verify(mailSenderService).send(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    public void testCreateUser_FileNotEmptyAndImageExists() throws IOException {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("image.png");
        when(Files.exists(Path.of(System.getProperty("user.dir") + "/uploads/images/image.png"))).thenReturn(true);

        userService.createUser(user, file);

        verify(userImageService, never()).saveImage(file);
        verify(userRepository).save(user);
        verify(mailSenderService).send(eq(user.getEmail()), anyString(), anyString());
    }
}