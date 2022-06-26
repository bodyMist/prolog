package kit.prolog.repository;

import kit.prolog.domain.User;
import kit.prolog.repository.jpa.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    @Test
    void 유저조회(){
        User user = userRepository.findOneById(1L);
        System.out.println(user.getName());
    }

    @Test
    void 회원가입(){
        User user = new User();

        user.setAccount("asdf111");
        user.setPassword("asdf12!");
        user.setEmail("tkdrms0301@naver.com");
        user.setName("안상근");
        user.setImage("");
        user.setNickname("An");
        user.setIntroduce("hello world!");
        user.setAlarm(true);
        userRepository.save(user);
    }

    @Test
    void 로그인(){
        String account = "asdf12";
        String password = "asdf12!";
        User user = userRepository.findByAccountAndPassword(account, password);
        System.out.println(user.getName());
        System.out.println(user.getAccount());
    }

    @Test
    void 회원수정(){
        Long memberpk = 1L;
        User user = userRepository.findOneById(memberpk);
        user.setAccount("asdf111");
        user.setPassword("asdf12!");
        user.setEmail("tkdrms0301@naver.com");
        user.setName("안상근");
        user.setImage("");
        user.setNickname("An");
        user.setIntroduce("hello world!");
        user.setAlarm(true);
        userRepository.save(user);
    }

    @Test
    void 회원탈퇴(){
        Long memberpk = 1L;
        User user = userRepository.findOneById(memberpk);
        System.out.println(user.getName());
        userRepository.deleteById(memberpk);
        //return user;
    }
}
