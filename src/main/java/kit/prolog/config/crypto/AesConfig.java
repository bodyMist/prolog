//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package kit.prolog.config.crypto;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kit.prolog.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class AesConfig {
    private final String AES_KEY_PATH = "src/main/resources/key/aes.txt";
    private final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private final FileUtil fileUtil;

    public String encrypt(List<String> aesKey, String plainText) {
        if (plainText.equals("")) {
            return plainText;
        }

        String sAesKey = aesKey.get(0).substring(0, 32);
        String sVi = aesKey.get(1).substring(0, 16);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(sAesKey.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(sVi.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
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
        } catch (InvalidAlgorithmParameterException e) {
            log.info("잘못된 초기 벡터 사용");
        }
        return plainText;
    }

    public String decrypt(List<String> aesKey, String cipherText) {
        if (cipherText.equals("")) {
            return cipherText;
        }

        String sAesKey = aesKey.get(0).substring(0, 32);
        byte[] bAesKey = Base64.getDecoder().decode(sAesKey.getBytes(StandardCharsets.UTF_8));

        String sVi = aesKey.get(1).substring(0, 16);
        byte[] bVi = Base64.getDecoder().decode(sVi.getBytes(StandardCharsets.UTF_8));

        try {

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(bAesKey, "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(bVi);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

            byte[] encrypted = cipher.doFinal(cipherText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);

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
        } catch (InvalidAlgorithmParameterException e) {
            log.info("잘못된 초기 벡터 사용");
        }

        return cipherText;
    }

    public List<String> keyRead() {
        return fileUtil.fileRead(AES_KEY_PATH);
    }

    public void keyWrite(List<String> lines) {
        fileUtil.fileWrite(lines, AES_KEY_PATH);
    }
}
