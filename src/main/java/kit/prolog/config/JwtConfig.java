package kit.prolog.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Configuration
@PropertySource("classpath:jwt/jwt.properties")
@Getter
public class JwtConfig {
    @Value("${spring.jwt.key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        // 객체 초기화, secretKey를 Base64로 인코딩한다.
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
}
