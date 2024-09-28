package kolya.study.bookservice.service;

import kolya.study.bookservice.exception.PdfConversionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class PdfConverter {
    @Value("${file.upload-dir}")
    private final String path = null;
    public void convertPdfToText(MultipartFile pdfFile) throws PdfConversionException {
        try {
            PDDocument document = PDDocument.load(pdfFile.getInputStream());
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);
            document.close();
            Path fileName = Paths.get((path + "\\ "  + pdfFile.getOriginalFilename()).replace(".pdf", ".txt"));
            Files.write(fileName, text.getBytes());
            log.info("Текст успішно збережений у файл: " + fileName);
        } catch (IOException e) {
            log.info("Помилка під час конвертації pdf файлу в формат txt ");
            throw new PdfConversionException("Помилка під час конвертації pdf файлу в формат txt");
        }


    }

}
