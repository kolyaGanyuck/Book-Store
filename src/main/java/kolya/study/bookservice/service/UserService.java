package kolya.study.bookservice.service;

import kolya.study.bookservice.dto.UserDto;
import kolya.study.bookservice.entity.Role;
import kolya.study.bookservice.entity.User;
import kolya.study.bookservice.mapper.UserMapper;
import kolya.study.bookservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserImageService userImageService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleService roleService;

    public Optional<UserDto> createUser(User user, MultipartFile file) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            return Optional.empty();
        } else {
            setUserDetails(user, file);
            userImageService.saveImage(file);
            userRepository.save(user);
            UserDto userDto = userMapper.map(user);
            return Optional.ofNullable(userDto);
        }
    }

    public User setUserDetails(User user, MultipartFile file) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProfileImage(file.getOriginalFilename());
        Role roleUser = roleService.findRoleOrCreate("ROLE_USER");
        user.getAuthorities().add(roleUser);
        return user;
    }

    public UserDto getUser(Long id){
        Optional<User> userById = userRepository.findById(id);
        return userById.map(userMapper::map).orElse(null);
    }

}
