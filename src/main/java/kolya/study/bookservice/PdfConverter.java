package kolya.study.bookservice;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component

public class PdfConverter {

    public void convertPdfToText(MultipartFile pdfFile){
        try {
            PDDocument document = PDDocument.load(pdfFile.getInputStream());
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);
            document.close();
            Path fileName = Paths.get(("D:/uploads2B/ " + pdfFile.getOriginalFilename()).replace(".pdf", ".txt"));
            Files.write(fileName, text.getBytes());
            System.out.println("Текст успішно збережений у файл: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
