package kolya.study.bookservice;

import ch.qos.logback.core.model.ModelConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
@Slf4j
@Service
public class ImageService {
    public void getImageCoverFromPdf(MultipartFile pdfFile, Book book){
        try {
            PDDocument document = PDDocument.load(pdfFile.getInputStream());
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            baos.flush();

            String coverImage = Base64.getEncoder().encodeToString(baos.toByteArray());
            book.setImageCover(coverImage);

            book.setTitle(Objects.requireNonNull(pdfFile.getOriginalFilename()).substring(0, pdfFile.getOriginalFilename().length() - 4));

            baos.close();
            document.close();
            log.info("Зображення збережено" );
        } catch (IOException e) {
            log.info("Помилка під час збереження зображення");
            throw new RuntimeException(e);
        }
    }


}
