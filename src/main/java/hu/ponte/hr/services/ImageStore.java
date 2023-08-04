package hu.ponte.hr.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Paths;
import hu.ponte.hr.controller.ImageMeta;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageStore {
    private final SignService signService;
    private final List<ImageMeta> images = new ArrayList<>(); // Metaadatok tárolása.
    private final Map<String, byte[]> imageData = new HashMap<>(); // Képek biteadatai.

    @Autowired
    public ImageStore(SignService signService) {
        this.signService = signService;
    }

    // Kép mentése
    public void saveImage(MultipartFile file) throws IOException {
        String id = UUID.randomUUID().toString();
        String name = file.getOriginalFilename();
        String mimeType = file.getContentType();
        long size = file.getSize();
        byte[] fileBytes = file.getBytes();
        String digitalSign = signService.sign(fileBytes); // Ez felel a digitális aláírásért.

        String path = "C:/Users/V2/Desktop/" + name; // FONTOS!!!!!! EZT ITT ÁT KELL ÍRNI, HOGY HOVA MENTSE!!!!!!

        ImageMeta imageMeta = ImageMeta.builder()
                .id(id)
                .name(name)
                .mimeType(mimeType)
                .size(size)
                .digitalSign(digitalSign)
                .path(path)
                .build();

        images.add(imageMeta); // Metaadatok hozzáadása
        imageData.put(id, fileBytes); // Bytok hozzáadása a maphez

        Files.write(Paths.get(path), fileBytes); // Filerendszerbe mentés
    }

    // Az összes metaadat lekérdezése
    public List<ImageMeta> getAllImagesMeta() {
        return new ArrayList<>(images);
    }

    // Egy kép lekérdezése ID alapján
    public ResponseEntity<byte[]> getImage(String id, HttpServletResponse response) {
        Optional<ImageMeta> optionalImageMeta = images.stream()
                .filter(imageMeta -> imageMeta.getId().equals(id))
                .findFirst();

        // Ha megvan a kép, beolvassa a filet és a válasz tartalma is elkészül.
        if (optionalImageMeta.isPresent()) {
            ImageMeta imageMeta = optionalImageMeta.get();
            byte[] imageBytes = new byte[0];
            try {
                imageBytes = Files.readAllBytes(Paths.get(imageMeta.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageBytes.length > 0) {
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMeta.getMimeType())).body(imageBytes);
            }
        }
        return ResponseEntity.notFound().build(); // NotFound státuszkód, ha nincs meg a kép.
    }
}
