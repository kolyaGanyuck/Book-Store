package kolya.study.bookservice.controller;

import kolya.study.bookservice.entity.User;
import kolya.study.bookservice.repository.UserRepository;
import kolya.study.bookservice.service.UserImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserImageService userImageService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/create")
    public String formUser() {
        return "user/create-user";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam("file") MultipartFile file, @ModelAttribute User user, Model model) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            model.addAttribute("userExist", byUsername.get().getUsername());
            return "user/create-user";
        } else {
            userImageService.saveImage(file);
            user.setProfileImage(file.getOriginalFilename());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            model.addAttribute("user", user);
            return "catalogue";
        }
    }

    @GetMapping("/user-profile/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            return "user/profile";
        }
        return "redirect:/error";
    }
}

