package kit.prolog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailAuthServiceTest {
    @Autowired
    EmailAuthService emailAuthService;

    @Test
    void 인증번호(){
        System.out.println(emailAuthService.makeEmailAuthNumber());
    }

    @Test
    void 메일발송(){
        //emailAuthService.sendMail("tkdrms0301@naver.com");
        emailAuthService.sendMail("ansang01234@gmail.com");
    }
}
