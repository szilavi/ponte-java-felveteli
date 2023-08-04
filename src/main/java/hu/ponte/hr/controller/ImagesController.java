package hu.ponte.hr.controller;

import hu.ponte.hr.dto.ImageDetails;
import hu.ponte.hr.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/images")
public class ImagesController {

    private final ImageService imageService;

    @Autowired
    public ImagesController(ImageService imageService) {
        this.imageService = imageService;
    }

    //Az api/images/meta végpontot kezelem itt.
    @GetMapping("/meta")
    public ResponseEntity<List<ImageDetails>> listImages() {
        return new ResponseEntity<>(imageService.getAllImagesMeta(), HttpStatus.OK); // A visszatérési érték itt a képek metaadatai.
    }

    // Az api/images/preview/{id} GET kéréseit kezelem itt.
    @GetMapping("/preview/{id}")
    public ResponseEntity<ImageDetails> getImage(@PathVariable("id") String id) {
        ImageDetails imageDetails;
        try {
            imageDetails = imageService.getImage(id);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Visszatér egy ResponseEntity formában.
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageDetails.getMimeType())).body(imageDetails);
    }
}
