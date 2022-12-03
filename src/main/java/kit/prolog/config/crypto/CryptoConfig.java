package kit.prolog.config.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class CryptoConfig {
    private final AesConfig aesConfig;
    private final RsaConfig rsaConfig;

    public List<String> keyConfig() {
        List<String> rsaKey = rsaConfig.keyRead();
        List<String> aesKey = aesConfig.keyRead();
        List<String> newAesKey = new ArrayList<>();

        newAesKey.add(rsaConfig.decrypt(rsaKey, aesKey.get(0)));
        newAesKey.add(rsaConfig.decrypt(rsaKey, aesKey.get(1)));
        return newAesKey;
    }

    public List<String> rsaKeyCreate() {
        List<String> originalAesKey = keyConfig(); // 기존에 rsa키로 암호화된 aes키 복호화
        List<String> newRsaKey = rsaConfig.rsaKeyCreate(); // rsa키 생성
        List<String> aesKeyByEnNewRsaKey = new ArrayList<>(); // 생성한 rsa 키로 aes키 암호화
        aesKeyByEnNewRsaKey.add(rsaConfig.encrypt(newRsaKey, originalAesKey.get(0)));
        aesKeyByEnNewRsaKey.add(rsaConfig.encrypt(newRsaKey, originalAesKey.get(1)));

        rsaConfig.keyWrite(newRsaKey); // rsa 키 파일에 쓰기
        return newRsaKey;
    }

    public List<String> aesKeyCreate() {
        List<String> newAesKey = new ArrayList<>();
        String aes = hash(String.valueOf(System.currentTimeMillis()));
        String iv = hash(aes);
        newAesKey.add(aes);
        newAesKey.add(iv);
        aesConfig.keyWrite(newAesKey);
        return newAesKey;
    }

    public String encrypt(List<String> decryptedAesKey, String plainText) {
        return aesConfig.encrypt(decryptedAesKey, plainText);
    }

    public String decrypt(List<String> decryptedAesKey, String cipherText) {
        return aesConfig.decrypt(decryptedAesKey, cipherText);
    }

    public String hash(String seed) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(seed.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
