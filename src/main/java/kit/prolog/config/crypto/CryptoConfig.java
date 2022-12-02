//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package kit.prolog.config.crypto;

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

    public String encrypt(List<String> decryptedAesKey, String plainText) {
        return aesConfig.encrypt(decryptedAesKey, plainText);
    }

    public String decrypt(List<String> decryptedAesKey, String cipherText) {
        return aesConfig.decrypt(decryptedAesKey, cipherText);
    }
}
