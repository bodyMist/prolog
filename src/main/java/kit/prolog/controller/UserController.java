package kit.prolog.controller;

import kit.prolog.domain.User;
import kit.prolog.dto.SuccessDto;
import kit.prolog.service.EmailAuthService;
import kit.prolog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/signup/email")
    public SuccessDto createUserByEmail(@RequestBody String name,
                                        @RequestBody String account,
                                        @RequestBody String password,
                                        @RequestBody String email,
                                        @RequestBody boolean alarm,
                                        @RequestBody String image,
                                        @RequestBody String nickname,
                                        @RequestBody String introduction){
        User user = new User();
        user.setName(name);
        user.setAccount(account);
        user.setPassword(password);
        user.setEmail(email);
        user.setAlarm(alarm);
        user.setImage(image);
        user.setNickname(nickname);
        user.setIntroduce(introduction);

        if(userService.createUserByEmail(user)){
            return new SuccessDto(true, null);
        }else{
            return new SuccessDto(false, null);
        }
    }

    @GetMapping("/signup/:social")
    public SuccessDto createUserBySocial(@RequestBody String name,
                                         @RequestBody String account,
                                         @RequestBody String password,
                                         @RequestBody String email,
                                         @RequestBody boolean alarm,
                                         @RequestBody String image,
                                         @RequestBody String nickname,
                                         @RequestBody String introduction){
        User user = new User();
        user.setName(name);
        user.setAccount(account);
        user.setPassword(password);
        user.setEmail(email);
        user.setAlarm(alarm);
        user.setImage(image);
        user.setNickname(nickname);
        user.setIntroduce(introduction);

        if(userService.createUserBySocial(user)){
            return new SuccessDto(true, null);
        }else{
            return new SuccessDto(false, null);
        }
    }

     //memberpk로 유저 정보 검색
     //readUser()
     //검색된 유저 정보 반환
    @GetMapping("/my-info")
    public SuccessDto readUser(@RequestHeader(value = "memberpk")Long userId){
        User user = userService.readUser(userId);
        if(user.getId() != 0){
            return new SuccessDto(true, user);
        }else{
            return new SuccessDto(false, null);
        }

    }

    //memberpk로 유저 정보 검색
    //deleteUser()
    //삭제된 유저 정보 반환


    @GetMapping("/memberout")
    public SuccessDto deleteUser(@RequestHeader(value = "memberpk")Long userId){
        User user = userService.deleteUser(userId);
        if(user.getId() != 0){
            return new SuccessDto(true, user);
        }else{
            return new SuccessDto(false, null);
        }
    }

    @PutMapping("/my-info")
    public SuccessDto updateUser(@RequestHeader(value = "memberpk")Long userId, @RequestBody User user){
        User modifiedUser = userService.updateUser(userId, user);
        if(modifiedUser != null){
            return new SuccessDto(true, modifiedUser);
        }else{
            return new SuccessDto(false, null);
        }

    }

    @PostMapping("/updatepw")
    public SuccessDto changePassword(@RequestBody String account,
                                     @RequestBody String password){
        if(userService.changePassword(account, password)){

            return new SuccessDto(true, null);
        }else {
            return new SuccessDto(false, null);
        }

    }

    @PostMapping("/login")
    public SuccessDto login(@RequestBody String account,
                            @RequestBody String password){
        if(userService.changePassword(account, password)){
            return new SuccessDto(true, null); // memberpk 추가 필요
        }else {
            return new SuccessDto(false, null);
        }
    }

    @PostMapping("/idauth")
    public SuccessDto searchAccount(@RequestBody String email){
        String account = userService.searchAccount(email);
        if(account != null){
            return new SuccessDto(true, account);
        }else{
            return new SuccessDto(false, null);
        }
    }

    @PostMapping("/email")
    public SuccessDto sendMail(@RequestBody String email){
        if(userService.searchAccount(email) != null){
            int emailAuthNumber = emailAuthService.sendMail(email);
            return new SuccessDto(true, null);
        }else{
            return new SuccessDto(false, null);
        }
    }

    @PostMapping("/email/auth")
    public SuccessDto emailAuth(int emailAuthNumber){
        int makeAuthNumber = 0; // 받아와야함
        if(emailAuthService.emailAuth(emailAuthNumber, makeAuthNumber)){
            return new SuccessDto(true, null);
        }else{
            return new SuccessDto(false, null);
        }
    }

    // 이메일 시간 단위 인증
    // 현재 시간을 날짜, 시간, 분, 초 단위까지 데이터 저장
    // mysql 에 새로운 테이블 필요 -> 데이터는 id(AutoIncrement), email(유저가 입력한 email), 시간데이터, 인증번호
    // 저장된 데이터에 +3분 한 시간 인증번호 발송과 함께 프론트에 전달
    // 프론트는 해당 시간까지 전송이 가능하도록 함
    // 백엔드는 해당 시간까지 데이터를 받을 수 있음
    // 받아야하는 데이터 인증번호만, 현재 시간을 기준으로 판단함
}
