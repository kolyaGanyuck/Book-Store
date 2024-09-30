package kolya.study.bookservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import java.util.Objects;

@Service
public class UserImageService {
    private final String uploadDirectory = System.getProperty("user.dir") + "/uploads/images/";

    public void saveImage(MultipartFile file) {
        Path filePath = Paths.get(uploadDirectory + Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Files.createDirectories(filePath.getParent());
            file.transferTo(new File(filePath.toString()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e);
        }
    }
}
