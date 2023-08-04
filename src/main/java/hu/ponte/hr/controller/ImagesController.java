package hu.ponte.hr.controller;

import hu.ponte.hr.services.ImageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("api/images")
public class ImagesController {
    @Autowired
    private ImageStore imageStore;

    //Az api/images/meta végpontot kezelem itt.
    @GetMapping("meta")
    public List<ImageMeta> listImages() {
        return imageStore.getAllImagesMeta(); // A visszatérési érték itt a képek metaadatai.
    }

    // Az api/images/preview/{id} GET kéréseit kezelem itt.
    @GetMapping("preview/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id, HttpServletResponse response) {
        return imageStore.getImage(id, response); // Visszatér egy ResponseEntity formában.
    }
}
