package hu.ponte.hr.service;

import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import hu.ponte.hr.dto.ImageDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ImageService {
    private final SignService signService;
    private final List<ImageDetails> images = new CopyOnWriteArrayList<>(); // Metaadatok tárolása.
    private final Map<String, byte[]> imageData = new ConcurrentHashMap<>(); // Képek biteadatai.

    @Autowired
    public ImageService(SignService signService) {
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

        ImageDetails imageDetails = ImageDetails.builder()
                .id(id)
                .name(name)
                .mimeType(mimeType)
                .size(size)
                .digitalSign(digitalSign)
                .path(path)
                .build();

        images.add(imageDetails); // Metaadatok hozzáadása
        imageData.put(id, fileBytes); // Bytok hozzáadása a maphez

        Files.write(Paths.get(path), fileBytes); // Filerendszerbe mentés
    }

    // Az összes metaadat lekérdezése
    public List<ImageDetails> getAllImagesMeta() {
        return new ArrayList<>(images);
    }

    // Egy kép lekérdezése ID alapján
    public ImageDetails getImage(String id) throws IOException, NoSuchElementException {
        ImageDetails imageDetails = images.stream()
                .filter(image -> image.getId().equals(id))
                .findFirst().orElseThrow();

        imageDetails.setFile(Files.readAllBytes(Paths.get(imageDetails.getPath())));

        // Ha megvan a kép, beolvassa a filet és a válasz tartalma is elkészül.

        // NotFound státuszkód, ha nincs meg a kép.
        return imageDetails;
    }
}
