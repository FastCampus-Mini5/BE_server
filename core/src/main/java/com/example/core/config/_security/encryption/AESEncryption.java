package com.example.core.config._security.encryption;

import com.example.core.errors.exception.DecryptException;
import com.example.core.errors.exception.EncryptException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AESEncryption implements Encryption {

    private static final String AES_ALGORITHM = "AES";

    @Value("${aes-key}")
    private static String AES_KEY;
    private final Environment environment;

    public String encrypt(String data) throws EncryptException {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception exception) {
            throw new EncryptException(exception.getMessage());
        }
    }

    public String decrypt(String encryptedData) throws DecryptException {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedData = Base64.getDecoder().decode(encryptedData);
            byte[] originalData = cipher.doFinal(decryptedData);

            return new String(originalData);
        } catch (Exception exception) {
            throw new DecryptException(exception.getMessage());
        }
    }

    @PostConstruct
    private void init() {
        AES_KEY = environment.getProperty("aes-key");
    }
}
