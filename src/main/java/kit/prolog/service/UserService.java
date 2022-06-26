package kit.prolog.service;

import kit.prolog.domain.User;
import kit.prolog.repository.jpa.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // email 회원가입
    public boolean createUserByEmail(User user){
        // userRepository.save(user);
        // error catch 추가 예정
        return true;
    }

    // 소셜 회원가입
    public boolean createUserBySocial(User user){
        // userRepository.save(user);
        // error catch 추가 예정
        return true;
    }

    // 회원 정보 조회
    public User readUser(Long memberPk){
        User user = new User();
        // user = userRepository.findOneById(memberPk);
        // user 존재하면 user 반환
        // 비어있는 user 반환
        return user;
    }

    // 회원 정보 수정
    public User updateUser(Long memberPk, User modifiedUser){
        User user = new User();
        /*user = userRepository.findOneById(memberPk);
        user.setName(modifiedUser.getName());
        user.setImage(modifiedUser.getImage());
        user.setIntroduce(modifiedUser.getIntroduce());
        user.setNickname(modifiedUser.getNickname());
        user.setAlarm(modifiedUser.getAlarm());
        userRepository.save(user);*/
        return user;
    }

    // 회원 탈퇴
    public User deleteUser(Long memberPk){
        User user = new User();
        /*user = userRepository.findOneById(memberPk);
        userRepository.deleteById(memberPk);*/
        return user;
    }

    // 로그인
    public boolean login(String account, String password){
        User user = new User();
        /*user = userRepository.findByAccountAndPassword(account, password);*/
        return true;
    }

    // 아이디 찾기
    public String searchAccount(String email){
        User user = new User();
        user = userRepository.findOneByEmail(email);
        return user.getAccount();
    }

    // 비밀번호 변경
    /*public String changePassword(){
        String password = "";
        return password;
    }*/

    // email 발송
/*    public void sendEmail(){

    }*/

    // email 인증
/*    public void emailAuth(){

    }*/
}
