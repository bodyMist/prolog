package kit.prolog.controller;

import kit.prolog.domain.User;
import kit.prolog.domain.redis.JwtAuthToken;
import kit.prolog.dto.*;
import kit.prolog.service.EmailAuthService;
import kit.prolog.service.JwtService;
import kit.prolog.service.RedisService;
import kit.prolog.service.UserService;
import kit.prolog.service.social.GithubAuthService;
import kit.prolog.service.social.KakaoAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final RedisService redisService;
    private final EmailAuthService emailAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final GithubAuthService githubAuthService;
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
        user.setSns(0); // sns { 0번 : email, 1번 : kakao로그인 2번 : github }

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
    @GetMapping("/my-info")
    public SuccessDto readUser(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestHeader(value = "refreshToken") String refreshToken){
        if(jwtService.validateToken(accessToken)){
            String userId = jwtService.getUserPk(accessToken);
            User user = userService.readUser(Long.parseLong(userId));
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
        }else if(jwtService.validateToken(refreshToken)){
            // jwt accessToken 인증시간 초과
            return new SuccessDto(false, "access toekn invalid");
        }else{
            // jwt accessToken, jwt refreshToken 인증시간 초과
            return new SuccessDto(false, "access toekn & refresh token invalid");
        }
    }


    //memberpk로 유저 정보 검색
    //deleteUser()
    //삭제된 유저 정보 반환
    @GetMapping("/memberout")
    public SuccessDto deleteUser(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestHeader(value = "refreshToken") String refreshToken){
        if(jwtService.validateToken(accessToken)){
            String userId = jwtService.getUserPk(accessToken);
            User user = userService.readUser(Long.parseLong(userId));
            if(user.getId() != 0){
                userService.deleteUser(Long.parseLong(userId));
                return new SuccessDto(true, user.getId());
            }else{
                return new SuccessDto(false, null);
            }
        }else if(jwtService.validateToken(refreshToken)){
            // jwt accessToken 인증시간 초과
            return new SuccessDto(false, "access toekn invalid");
        }else{
            // jwt accessToken, jwt refreshToken 인증시간 초과
            return new SuccessDto(false, "access toekn & refresh token invalid");
        }
    }


    @PutMapping("/my-info-update")
    public SuccessDto updateUser(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestHeader(value = "refreshToken") String refreshToken,
            @RequestBody UserInfoDto userInfoDto){
        if(jwtService.validateToken(accessToken)){
            String userId = jwtService.getUserPk(accessToken);
            User user = userService.readUser(Long.parseLong(userId));
            if(userService.updateUser(Long.parseLong(userId), userInfoDto)){
                return new SuccessDto(true, null);
            }else{
                return new SuccessDto(false, null);
            }
        }else if(jwtService.validateToken(refreshToken)){
            // jwt accessToken 인증시간 초과
            return new SuccessDto(false, "access toekn invalid");
        }else{
            // jwt accessToken, jwt refreshToken 인증시간 초과
            return new SuccessDto(false, "access toekn & refresh token invalid");
        }
    }

    @PostMapping("/updatepw")
    public SuccessDto changePassword(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestHeader(value = "refreshToken") String refreshToken,
            @RequestBody UserPwChangeDto userPwChangeDto){
        if(jwtService.validateToken(accessToken)){
            if(userService.changePassword(userPwChangeDto.getEmail(), userPwChangeDto.getAccount(), userPwChangeDto.getPassword())){
                return new SuccessDto(true, null);
            }else {
                return new SuccessDto(false, null);
            }
        }else if(jwtService.validateToken(refreshToken)){
            // jwt accessToken 인증시간 초과
            return new SuccessDto(false, "access toekn invalid");
        }else{
            // jwt accessToken, jwt refreshToken 인증시간 초과
            return new SuccessDto(false, "access toekn & refresh token invalid");
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
    //kakao 인증 url
    //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=ac1a0ec2e424c32d5baa95bf6114d8b0&redirect_uri=http://127.0.0.1:8080/login/kakao&scope=account_email,openid

    //github 인증 url
    //https://github.com/login/oauth/authorize?client_id=0006efe23ef0c6ecb6c0&redirect_uri=http://localhost:8080/login/github
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
    public SuccessDto loginByGithub(@RequestParam String code) throws IOException {
        System.out.println(code);
        String accessToken = githubAuthService.getGithubAccessToken(code);
        System.out.println(accessToken);
        githubAuthService.createGithubUser(accessToken);

        // jwt 추가
        return new SuccessDto(true, null);
    }

    @PostMapping("/logout/kakao")
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
}
