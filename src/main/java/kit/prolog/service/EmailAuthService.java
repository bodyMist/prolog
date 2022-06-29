package kit.prolog.service;

import kit.prolog.repository.jpa.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.Random;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
@PropertySource("classpath:mail/email.properties")
public class EmailAuthService {
    private final int EMAIL_AUTH_NUMBER_SIZE = 6;
    @Autowired
    private JavaMailSender mailSender;
    private final UserRepository userRepository;

    // email 발송
    public boolean sendMail(String email){
        int emailAuthNumber = makeEmailAuthNumber();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("ansang01234@gmail.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("이메일 인증");
        simpleMailMessage.setText(String.valueOf(emailAuthNumber));
        mailSender.send(simpleMailMessage);
        return true;
    }

    // email 인증
    public boolean emailAuth(int emailAuthNumber){
        // 이메일 인증 번호 비교 로직
        return true;
    }

    public int makeEmailAuthNumber(){
        Random random = new Random(System.currentTimeMillis());
        int num = random.nextInt(999999) + 100000;
        return num;
    }
}
