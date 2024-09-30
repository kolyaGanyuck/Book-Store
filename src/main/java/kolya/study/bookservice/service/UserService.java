package kolya.study.bookservice.service;

import kolya.study.bookservice.dto.UserDto;
import kolya.study.bookservice.entity.Role;
import kolya.study.bookservice.entity.User;
import kolya.study.bookservice.mapper.UserMapper;
import kolya.study.bookservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserImageService userImageService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final MailSenderService mailSenderService;
    private final String uploadDirectory = System.getProperty("user.dir") + "/uploads/images/anonymous.png";

    public Optional<UserDto> createUser(User user, MultipartFile file) throws IOException {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());

        if (byUsername.isPresent()) {
            return Optional.empty();
        } else {
            Path filePath = Path.of(uploadDirectory);

            // Якщо файл порожній, перевіряємо, чи файл з іменем "anonymous.png" вже існує
            if (file.isEmpty()) {
                if (!Files.exists(filePath)) {
                    // Створюємо файл тільки якщо його не існує
                    Files.createFile(filePath);
                }
            }

            // Задаємо деталі користувача
            setUserDetails(user, file);

            // Якщо файл ще не існує, зберігаємо його
            if (!Files.exists(filePath)) {
                userImageService.saveImage(file);
            }

            // Зберігаємо користувача в базі даних
            userRepository.save(user);

            // Відправляємо email з активаційним кодом
            String message = String.format("Hello %s, tap the link below to activate your account \n" +
                            "http://localhost:8081/user/%s",
                    user.getUsername(), user.getActivationCode());
            mailSenderService.send(user.getEmail(), "Activation code", message);

            // Перетворюємо користувача на DTO і повертаємо
            UserDto userDto = userMapper.map(user);
            return Optional.ofNullable(userDto);
        }
    }

    public User setUserDetails(User user, MultipartFile file) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProfileImage(file.getOriginalFilename());
        user.setActivationCode(UUID.randomUUID().toString());
        Role roleUser = roleService.findRoleOrCreate("ROLE_USER");
        user.getAuthorities().add(roleUser);
        return user;
    }

    public UserDto getUser(Long id) {
        Optional<User> userById = userRepository.findById(id);
        return userById.map(userMapper::map).orElse(null);
    }

    public UserDto checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof MyUserDetails userDetails) {
            UserDto userDto = getUser(userDetails.getId());
            if (userDto != null) {
                log.info("User ID: " + userDetails.getId());
                return userDto;
            }
        } else
            log.info("No authenticated user found.");
        return null;
    }

    public void confirmEmail(String activationCode) {
        Optional<User> optionalUser = userRepository.findByActivationCode(activationCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActivated(true);
            user.setActivationCode(null);
            userRepository.save(user);
        }

    }

}
