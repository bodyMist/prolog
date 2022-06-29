package kit.prolog.service;

import kit.prolog.domain.User;
import kit.prolog.repository.jpa.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // email 회원가입
    public boolean createUserByEmail(User user){
        if(userRepository.existsUserByEmail(user.getEmail())){
            userRepository.save(user);
        }
        // error catch 추가 예정
        return true;
    }

    // 소셜 회원가입
    public boolean createUserBySocial(User user){
        if(userRepository.existsUserByEmail(user.getEmail())){
            userRepository.save(user);
            // account = email
        }
        // error catch 추가 예정
        return true;
    }

    // 회원 정보 조회
    public User readUser(Long memberPk){
        User user = new User();
        try{
            user = userRepository.findOneById(memberPk);
        }catch (NullPointerException e){
            System.out.println("Error : no user");
        }
        return user;
        // user 존재하면 user 반환
        // 비어있는 user 반환
    }

    // 회원 정보 수정
    public User updateUser(Long memberPk, User modifiedUser){
        User user = new User();
        try{
            user = userRepository.findOneById(memberPk);
            user.setName(modifiedUser.getName());
            user.setImage(modifiedUser.getImage());
            user.setIntroduce(modifiedUser.getIntroduce());
            user.setNickname(modifiedUser.getNickname());
            user.setAlarm(modifiedUser.getAlarm());
            userRepository.save(user);
        }catch (NullPointerException e){
            System.out.println("Error : no user");
        }
        return user;
    }

    // 회원 탈퇴
    public User deleteUser(Long memberPk){
        User user = new User();
        try{
            user = userRepository.findOneById(memberPk);
            if(user != null)
                userRepository.deleteById(memberPk);
        }catch (NullPointerException e){
            System.out.println("Error : no user");
        }
        return user;
    }

    // 로그인
    public boolean login(String account, String password){
        User user;
        try{
            user = userRepository.findByAccountAndPassword(account, password);
            if(user != null)
                return true;
            else
                return false;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return false;
        }
    }

    // 아이디 찾기
    public String searchAccount(String email){
        User user;
        String account = "";
        try{
            user = userRepository.findOneByEmail(email);
            account = user.getAccount();
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return account;
        }
        return account;
    }

    // 비밀번호 변경을 위한 user 검색
    public boolean changePassword(String account, String password){
        User user;
        try{
            user = userRepository.findByAccountAndPassword(account, password);
            if(user != null)
                return true;
            else
                return false;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return false;
        }
    }
}
