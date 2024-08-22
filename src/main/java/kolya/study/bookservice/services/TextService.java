package kolya.study.bookservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class TextService {
    public List<String> paginateText(Path path) throws IOException {
        String text = getFile(path);
        String[] words = text.split("\\s+");
        List<String> pages = new ArrayList<>();
        StringBuilder page = new StringBuilder();
        int wordCount = 0;
        for (String word : words) {
            if (wordCount == 300) {
                pages.add(page.toString().trim());
                page = new StringBuilder();
                wordCount = 0;
            }
            page.append(word).append(" ");
            wordCount++;
        }
        if (page.length() > 0) {
            pages.add(page.toString().trim());
        }
        log.info("Текст розбито на сторінки");
        return pages;
    }

    public String getFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }
}
