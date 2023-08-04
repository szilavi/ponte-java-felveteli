package hu.ponte.hr.controller;

import hu.ponte.hr.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class UploadController
{
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024L; // 2MB

    private static final Logger log = LoggerFactory.getLogger(UploadController.class); // Ellenőriztem, hogy minden rendben megy-e

    private final ImageService imageService;

    @Autowired
    public UploadController(ImageService imageService) {
        this.imageService = imageService;
    }

    // api/file/post-ra érkező kérések
    @PostMapping("/post")
    public ResponseEntity<String> handleFormUpload(@RequestParam("file") MultipartFile file) {

        if (file.getSize() > MAX_FILE_SIZE) {
            return new ResponseEntity<>("Túl nagy a kép", HttpStatus.PAYLOAD_TOO_LARGE);
        } // Tesztelem, hogy af file nem nagyobb-e, mint 2MB

        try {
            log.info("POST kérés érkezett. Képfájl mentése..."); // Ellenőrzés miatt kellett.
            imageService.saveImage(file);
            log.info("Képfájl sikeresen mentve."); // Ha rendben lefut az ImageStore saveImage-ja, akkor kiloggolom, hogy eljutottunk odáig.
        } catch (IOException e) {
            log.error("Hiba történt a képfájl mentésekor.", e);
            return new ResponseEntity<>("Hiba történt a képfájl mentésekor.", HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("hiba a fájl aláírásakor", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("Image uploaded successfully!", HttpStatus.CREATED);
    }
}
