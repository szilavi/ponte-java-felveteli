package hu.ponte.hr.service;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class SignService {

    // Aláírja az adatokat egy privát kulccsal.
    public String sign(byte[] data) {
        try {
            // Itt olvassa be a kulcsot.
            byte[] keyBytes = Files.readAllBytes(Paths.get("./src/main/resources/config/keys/key.private"));
            // Itt előálítja a kulcsot
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(spec);

            // Az aláírás létrehozása privát kulccsal.
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(privateKey);
            privateSignature.update(data);

            // Elkészíti az aláírást.
            byte[] signature = privateSignature.sign();

            // Visszaadjuk, de Base64 formátumban.
            return Base64.getEncoder().encodeToString(signature);

        } catch (Exception e) {
            // Kivételt dobunk, ha baj van.
            //Generikus kivételt nem célszerű, hol kapjuk el? mit kezdünk vele? by Levi
            throw new RuntimeException("Could not sign the data", e);
        }
    }
}
