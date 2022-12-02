package kit.prolog.config.crypto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import kit.prolog.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class RsaConfig {
    private final String RSA_KEY_PATH = "src/main/resources/key/rsa.txt";
    private final String ALGORITHM = "RSA";
    private final FileUtil fileUtil;

    public List<String> rsaKeyCreate() {
        boolean retval = false;
        // 서버측 키 파일 생성 하기
        PublicKey publicKey1 = null;
        PrivateKey privateKey1 = null;

        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024, secureRandom);

            KeyPair keyPair = keyPairGenerator.genKeyPair();
            publicKey1 = keyPair.getPublic();
            privateKey1 = keyPair.getPrivate();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] bPublicKey1 = publicKey1.getEncoded();
        String sPublicKey1 =  Base64.getEncoder().encodeToString(bPublicKey1);

        byte[] bPrivateKey1 = privateKey1.getEncoded();
        String sPrivateKey1 =  Base64.getEncoder().encodeToString(bPrivateKey1);

        List<String> newRsaKey = new ArrayList<>();
        newRsaKey.add(sPublicKey1);
        newRsaKey.add(sPrivateKey1);
        return newRsaKey;
    }

    public String encrypt(List<String> rsaKey, String plainText) {
        if (plainText.equals("")) {
            return plainText;
        }

        byte[] bPublicKey = Base64.getDecoder().decode(rsaKey.get(0).getBytes(StandardCharsets.UTF_8));
        PublicKey publicKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bPublicKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bCipher = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(bCipher);
        } catch (NoSuchAlgorithmException e) {
            log.info("잘못된 암호화 알고리즘");
        } catch (NoSuchPaddingException e) {
            log.info("잘못된 패딩 알고리즘");
        } catch (InvalidKeyException e) {
            log.info("잘못된 키 사용");
        } catch (IllegalBlockSizeException e) {
            log.info("잘못된 블록 사이즈");
        } catch (BadPaddingException e) {
            log.info("패딩 에러");
        } catch (InvalidKeySpecException e) {
            log.info("잘못된 키 특성 사용");
        }

        return plainText;
    }

    public String decrypt(List<String> rsaKey, String cipherText) {
        if (cipherText.equals("")) {
            return cipherText;
        }

        byte[] bPrivateKey = Base64.getDecoder().decode((rsaKey.get(1)).getBytes(StandardCharsets.UTF_8));
        PrivateKey privateKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bPrivateKey);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] bCipher = Base64.getDecoder().decode(cipherText.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bPlain = cipher.doFinal(bCipher);
            return new String(bPlain);
        } catch (NoSuchAlgorithmException e) {
            log.info("잘못된 복호화 알고리즘");
        } catch (NoSuchPaddingException e) {
            log.info("잘못된 패딩 알고리즘");
        } catch (InvalidKeyException e) {
            log.info("잘못된 키 사용");
        } catch (IllegalBlockSizeException e) {
            log.info("잘못된 블록 사이즈");
        } catch (BadPaddingException e) {
            log.info("패딩 에러");
        } catch (InvalidKeySpecException e) {
            log.info("잘못된 키 특성 사용");
        }

        return cipherText;
    }

    public List<String> keyRead() {
        return fileUtil.fileRead(RSA_KEY_PATH);
    }

    public void keyWrite(List<String> lines) {
        fileUtil.fileWrite(lines, RSA_KEY_PATH);
    }
}
