package kit.prolog.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class EmailAuthService {
    @Autowired
    private JavaMailSender mailSender;

    // email 발송
    public int sendMail(String email){
        int emailAuthNumber = makeEmailAuthNumber();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("ansang01234@gmail.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("이메일 인증");
        simpleMailMessage.setText(String.valueOf(emailAuthNumber));
        mailSender.send(simpleMailMessage);
        return emailAuthNumber;
    }

    // email 인증
    // inputEmailAuthNumber 사용자 입력, emailAuthNumber 시스템 생성
    public boolean emailAuth(int inputEmailAuthNumber, int emailAuthNumber){
        if(inputEmailAuthNumber == emailAuthNumber){
            return true;
        }else{
            return false;
        }
    }

    // 랜덤한 숫자 시스템 생성
    public int makeEmailAuthNumber(){
        Random random = new Random(System.currentTimeMillis());
        int num = random.nextInt(900000) + 100000;
        return num;
    }
}
