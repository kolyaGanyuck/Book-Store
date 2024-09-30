package kolya.study.bookservice.controller;

import kolya.study.bookservice.dto.UserDto;
import kolya.study.bookservice.entity.User;
import kolya.study.bookservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/create")
    public String formUser() {
        return "user/create-user";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam("file") MultipartFile file, @ModelAttribute User user, Model model) throws IOException {
        Optional<UserDto> userOptional = userService.createUser(user, file);
        if (userOptional.isEmpty()) {
            model.addAttribute("userExist", user.getUsername());
            return "user/create-user";
        } else {
            model.addAttribute("user", userOptional);
            return "redirect:/books/catalogue";
        }
    }


    @GetMapping("/user-profile/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUser(id);
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
            return "user/profile";
        }
        return "redirect:/error";
    }

    @GetMapping("{activationCode}")
    public String activeProfile(@PathVariable String activationCode) {
        userService.confirmEmail(activationCode);
        return "redirect:/books/catalogue";
    }

}

