package kit.prolog.service.social;

import java.util.ArrayList;
import java.util.List;
import kit.prolog.config.crypto.CryptoConfig;
import kit.prolog.domain.User;
import kit.prolog.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CryptoUserService {
    private final UserRepository userRepository;
    private final CryptoConfig cryptoConfig;

    private User encrypt(List<String> decryptedAesKey, User newUser) {
        User user = new User();
        user.setId(newUser.getId());
        user.setAccount(cryptoConfig.encrypt(decryptedAesKey, newUser.getAccount()));
        user.setAlarm(newUser.getAlarm());
        user.setEmail(cryptoConfig.encrypt(decryptedAesKey, newUser.getEmail()));
        user.setImage(cryptoConfig.encrypt(decryptedAesKey, newUser.getImage()));
        user.setIntroduce(cryptoConfig.encrypt(decryptedAesKey, newUser.getIntroduce()));
        user.setName(cryptoConfig.encrypt(decryptedAesKey, newUser.getName()));
        user.setNickname(cryptoConfig.encrypt(decryptedAesKey, newUser.getNickname()));
        user.setPassword(newUser.getPassword());
        user.setSns(newUser.getSns());
        user.setSocialKey(cryptoConfig.encrypt(decryptedAesKey, newUser.getSocialKey()));
        return user;
    }

    private User decrypt(List<String> decryptedAesKey, User newUser) {
        User user = new User();
        user.setId(newUser.getId());
        user.setAccount(cryptoConfig.decrypt(decryptedAesKey, newUser.getAccount()));
        user.setAlarm(newUser.getAlarm());
        user.setEmail(cryptoConfig.decrypt(decryptedAesKey, newUser.getEmail()));
        user.setImage(cryptoConfig.decrypt(decryptedAesKey, newUser.getImage()));
        user.setIntroduce(cryptoConfig.decrypt(decryptedAesKey, newUser.getIntroduce()));
        user.setName(cryptoConfig.decrypt(decryptedAesKey, newUser.getName()));
        user.setNickname(cryptoConfig.decrypt(decryptedAesKey, newUser.getNickname()));
        user.setPassword(newUser.getPassword());
        user.setSns(newUser.getSns());
        user.setSocialKey(cryptoConfig.decrypt(decryptedAesKey, newUser.getSocialKey()));
        return user;
    }

    public void rsaKeyCreate() {
        cryptoConfig.rsaKeyCreate(); // rsaKey 생성 및 키 파일에 쓰기
    }

    public void aesKeyCreate() {
        List<User> deUserList = findAll(); // 복호화된 기존 유저 정보
        List<String> newAesKey = cryptoConfig.aesKeyCreate(); // 새로운 aesKey 생성 및 키 파일에 쓰기
        List<User> enUserList = new ArrayList<>(); // 새로운 aesKey로 aes 암호화
        for (User user : deUserList) {
            enUserList.add(decrypt(newAesKey, user));
        }
    }

    public List<User> findAll() {
        List<String> decryptedAesKey = cryptoConfig.keyConfig();
        List<User> decryptedUserList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            decryptedUserList.add(decrypt(decryptedAesKey, user));
        }
        return decryptedUserList;
    }

    public User save(User newUser) {
        List<String> decryptedAesKey = cryptoConfig.keyConfig();
        User enUser = encrypt(decryptedAesKey, newUser);
        return userRepository.save(enUser);
    }

    public User findOneByAccountAndEmail(String account, String email) {
        List<String> decryptedAesKey = cryptoConfig.keyConfig();
        String enAccount = cryptoConfig.encrypt(decryptedAesKey, account);
        String enEmail = cryptoConfig.encrypt(decryptedAesKey, email);
        User user = userRepository.findOneByAccountAndEmail(enAccount, enEmail);
        if (user != null)
            return decrypt(decryptedAesKey, user);
        return null;
    }

    public User findOneById(Long userId) {
        List<String> decryptedAesKey = cryptoConfig.keyConfig();
        User user = userRepository.findOneById(userId);
        if (user != null)
            return decrypt(decryptedAesKey, user);
        return null;
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findOneByAccountAndPassword(String account, String password) {

        List<String> decryptedAesKey = cryptoConfig.keyConfig();
        String enAccount = cryptoConfig.encrypt(decryptedAesKey, account);
        User user = userRepository.findOneByAccountAndPassword(enAccount, password);
        if (user != null)
            return decrypt(decryptedAesKey, user);
        return null;
    }

    public User findOneByEmail(String email) {
        List<String> decryptedAesKey = cryptoConfig.keyConfig();
        String enEmail = cryptoConfig.encrypt(decryptedAesKey, email);
        User user = userRepository.findOneByEmail(enEmail);
        if (user != null)
            return decrypt(decryptedAesKey, user);
        return null;
    }

    public User findOneBySnsAndSocialKey(Integer socialType, String socialKey) {
        List<String> decryptedAesKey = cryptoConfig.keyConfig();
        String enSocialKey = cryptoConfig.encrypt(decryptedAesKey, socialKey);
        User user = userRepository.findOneBySnsAndSocialKey(socialType, enSocialKey);
        if (user != null)
            return decrypt(decryptedAesKey, user);
        return null;
    }
}
