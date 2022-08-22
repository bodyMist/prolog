package kit.prolog.controller;

import kit.prolog.domain.User;
import kit.prolog.domain.redis.JwtAuthToken;
import kit.prolog.dto.*;
import kit.prolog.service.EmailAuthService;
import kit.prolog.service.JwtService;
import kit.prolog.service.RedisService;
import kit.prolog.service.UserService;
import kit.prolog.service.social.KakaoAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final RedisService redisService;
    private final EmailAuthService emailAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final JwtService jwtService;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PostMapping("/signup/email")
    public SuccessDto createUserByEmail(@RequestBody UserInfoDto userInfoDto){
        User user = new User();
        user.setName(userInfoDto.getName());
        user.setAccount(userInfoDto.getAccount());
        user.setPassword(userInfoDto.getPassword());
        user.setEmail(userInfoDto.getEmail());
        user.setAlarm(userInfoDto.isAlarm());
        user.setImage(userInfoDto.getImage());
        user.setNickname(userInfoDto.getNickname());
        user.setIntroduce(userInfoDto.getIntroduction());

        if(userService.createUserByEmail(user)){
            return new SuccessDto(true, null);
        }else{
            return new SuccessDto(false, null);
        }
    }

    @GetMapping("/signup/{social}")
    public SuccessDto createUserBySocial(@PathVariable String social){
        return new SuccessDto(true, social);
    }

     //memberpk로 유저 정보 검색
     //readUser()
     //검색된 유저 정보 반환
    @GetMapping("/my-info/{userId}")
    public SuccessDto readUser(@PathVariable Long userId){
        User user = userService.readUser(userId);
        if(user.getId() != 0){
            return new SuccessDto(true,
                    new UserInfoDto(
                            user.getName(),
                            user.getAccount(),
                            user.getPassword(),
                            user.getEmail(),
                            user.getAlarm(),
                            user.getImage(),
                            user.getNickname(),
                            user.getIntroduce()));
        }else{
            return new SuccessDto(false, null);
        }

    }


    //memberpk로 유저 정보 검색
    //deleteUser()
    //삭제된 유저 정보 반환
    @GetMapping("/memberout/{userId}")
    public SuccessDto deleteUser(@PathVariable Long userId){
        User user = userService.deleteUser(userId);
        if(user.getId() != 0){
            return new SuccessDto(true, user.getId());
        }else{
            return new SuccessDto(false, null);
        }
    }


    @PutMapping("/my-info-update/{userId}")
    public SuccessDto updateUser(@PathVariable Long userId, @RequestBody UserInfoDto userInfoDto){
        if(userService.updateUser(userId, userInfoDto)){
            return new SuccessDto(true, null);
        }else{
            return new SuccessDto(false, null);
        }

    }

    @PostMapping("/updatepw")
    public SuccessDto changePassword(@RequestBody UserPwChangeDto userPwChangeDto){
        if(userService.changePassword(userPwChangeDto.getEmail(), userPwChangeDto.getAccount(), userPwChangeDto.getPassword())){
            return new SuccessDto(true, null);
        }else {
            return new SuccessDto(false, null);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<SuccessDto> login(@RequestBody UserLoginDto userLoginDto){
        User user = userService.login(userLoginDto.getAccount(), userLoginDto.getPassword());
        if(user != null){
            HttpHeaders headers = new HttpHeaders();
            String accessToken = jwtService.createAccessToken(String.valueOf(user.getId()));
            String refreshToken = jwtService.createRefreshToken(String.valueOf(user.getId()));
            redisService.createJwtAuthToken(new JwtAuthToken(user.getId(), accessToken, refreshToken, null));
            headers.set("accessToken", accessToken);
            headers.set("refreshToken", refreshToken);
            return new ResponseEntity<SuccessDto>(new SuccessDto(true, user.getId()), headers, HttpStatus.valueOf(200));
        }else {
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, null), null, HttpStatus.valueOf(401));
        }
    }

    //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=ac1a0ec2e424c32d5baa95bf6114d8b0&redirect_uri=http://127.0.0.1:8080/login/kakao&scope=account_email,openid
    @GetMapping("/login/kakao")
    public SuccessDto loginByKakao(@RequestParam String code) {
        // 인가코드 받기
        String accessToken = kakaoAuthService.getKaKaoAccessToken(code);
        String email = kakaoAuthService.createKakaoUser(accessToken);
        if(email != null){
            User user = new User();
            user.setName("2391519776");
            user.setEmail(email);
            user.setAccount(email);
            user.setPassword("test");
            user.setAlarm(false);
            user.setSns(1);
            if(userService.createUserByEmail(user)){
                return new SuccessDto(true, null);
            }else{
                return new SuccessDto(false, null);
            }
        }else{
            return new SuccessDto(false, null);
        }
    }

    @GetMapping("/login/github")
    public SuccessDto loginByGithub(@RequestParam String code){
        System.out.println(code);
        System.out.println();
        // jwt 추가

        return new SuccessDto(true, null);
    }

    @PostMapping("/logout/kaako")
    public SuccessDto logout(){
        kakaoAuthService.logout("kfjZQIYglUAx3_K81StmRphjS-1JauB_j2Gz1hBvCj11mwAAAYK-tsdY");

        return new SuccessDto(true, null);
    }

    @PostMapping("/idauth")
    public SuccessDto searchAccount(@RequestBody UserEmailDto userEmailDto){
        String account = userService.searchAccount(userEmailDto.getEmail());
        if(account != null){
            return new SuccessDto(true, account);
        }else{
            return new SuccessDto(false, null);
        }
    }

    @PostMapping("/email")
    public SuccessDto sendMail(@RequestBody UserEmailDto userEmailDto){
        if(userService.searchAccount(userEmailDto.getEmail()) != null){
            int emailAuthNumber = emailAuthService.sendMail(userEmailDto.getEmail());
            if(redisService.createEmailAuthNumber(userEmailDto.getEmail(), emailAuthNumber)){
                return new SuccessDto(true, null);
            }else{
                return new SuccessDto(false, null);
            }
        }else{
            return new SuccessDto(false, null);
        }
    }

    @PostMapping("/email/auth")
    public SuccessDto emailAuth(@RequestBody UserEmailAuthNumber userEmailAuthNumber){
        int authNumber = redisService.readEmailAuthNumber(userEmailAuthNumber.getEmail());
        if(authNumber == 0){
            return new SuccessDto(false, null);
        } else if (authNumber == userEmailAuthNumber.getAuthNumber()) {
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
