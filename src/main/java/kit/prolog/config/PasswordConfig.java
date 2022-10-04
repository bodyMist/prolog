package kit.prolog.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

@Configuration
@PropertySource("classpath:password/password.properties")
@Getter
public class PasswordConfig {
    @Value("${spring.password.salt}")
    private String salt;
    private final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*#?&=^])[A-Za-z\\d@$!%*#?&=^]{8,24}$";
    private final String emailRegex = "^[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$";

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public String hashing(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        return bytesToHex(md.digest());
    }

    public boolean passwordValidataion(String password){
        return Pattern.matches(passwordRegex, password);
    }

    public boolean emailValidataion(String email){
        return Pattern.matches(emailRegex, email);
    }
}
