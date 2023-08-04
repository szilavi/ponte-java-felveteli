package hu.ponte.hr.controller.upload;

import hu.ponte.hr.services.ImageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequestMapping("api/file")
public class UploadController
{
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    private static final Logger log = LoggerFactory.getLogger(UploadController.class); // Ellenőriztem, hogy minden rendben megy-e

    @Autowired
    private ImageStore imageStore;

    // api/file/post-ra érkező kérések
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) {

        if (file.getSize() > MAX_FILE_SIZE) {
            return "Túl nagy a kép";
        } // Tesztelem, hogy af file nem nagyobb-e, mint 2MB

        try {
            log.info("POST kérés érkezett. Képfájl mentése..."); // Ellenőrzés miatt kellett.
            imageStore.saveImage(file);
            log.info("Képfájl sikeresen mentve."); // Ha rendben lefut az ImageStore saveImage-ja, akkor kiloggolom, hogy eljutottunk odáig.
        } catch (IOException e) {
            log.error("Hiba történt a képfájl mentésekor.", e);
            return "Error saving image";
        }

        return "Image uploaded successfully!";
    }
}
