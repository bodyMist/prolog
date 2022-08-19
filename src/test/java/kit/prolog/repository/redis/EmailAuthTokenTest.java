package kit.prolog.repository.redis;

import kit.prolog.domain.redis.EmailAuthToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailAuthTokenTest {
    @Autowired
    EmailAuthTokenRedisRepository emailAuthTokenRedisRepository;

    @Test
    void 이메일인증번호쓰기(){
        emailAuthTokenRedisRepository.save(new EmailAuthToken("ansang01234@gmail.com", 111111, 180L));
    }

    @Test
    void 이메일인증번호읽기(){
        System.out.println(emailAuthTokenRedisRepository.findOneByEmail("ansang01234@gmail.com"));
    }
}
