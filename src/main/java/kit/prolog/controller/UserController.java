package kit.prolog.controller;

import kit.prolog.config.PasswordConfig;
import kit.prolog.config.crypto.AesConfig;
import kit.prolog.config.crypto.CryptoConfig;
import kit.prolog.config.crypto.RsaConfig;
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

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final RedisService redisService;
    private final EmailAuthService emailAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final GithubAuthService githubAuthService;
    private final JwtService jwtService;
    private final PasswordConfig passwordConfig;
    private final AesConfig aesConfig;
    private final RsaConfig rsaConfig;
    private final CryptoConfig cryptoConfig;
    @PostMapping("/test")
    public SuccessDto test(){
        List<String> rsaKey = cryptoConfig.keyConfig();
        System.out.println(rsaKey);
        return new SuccessDto(false, "signup fail");
    }

    @PostMapping("/test2")
    public SuccessDto test2(){
        List<String> rsaKey = rsaConfig.keyRead();
        String test = rsaConfig.encrypt(rsaKey, "aaaaaaaaaabbbbbbbbbbccccccccccdd");
        System.out.println(test);
        System.out.println(rsaConfig.decrypt(rsaKey, test));
        return new SuccessDto(false, "signup fail");
    }

    @PostMapping("/signup/email")
    public SuccessDto createUserByEmail(@RequestBody UserEmailInfoDto userEmailInfoDto){
        String password = "";
        // password validation
        if(!passwordConfig.passwordValidataion(userEmailInfoDto.getPassword())){
            return new SuccessDto(false, "password invalid error");
        }
        // email validation
        if(!passwordConfig.emailValidataion(userEmailInfoDto.getEmail())){
            return new SuccessDto(false, "email invalid error");
        }
        // password hashing
        try{
            password = passwordConfig.hashing(passwordConfig.getSalt() + userEmailInfoDto.getPassword());
        }catch (NoSuchAlgorithmException e){
            return new SuccessDto(false, "signup fail");
        }

        User user = new User();
        user.setName(userEmailInfoDto.getName());
        user.setAccount(userEmailInfoDto.getAccount());
        user.setPassword(password);
        user.setEmail(userEmailInfoDto.getEmail());
        user.setAlarm(userEmailInfoDto.isAlarm());
        user.setImage(userEmailInfoDto.getImage());
        user.setNickname(userEmailInfoDto.getNickname());
        user.setIntroduce(userEmailInfoDto.getIntroduce());
        user.setSns(0); // sns { 0번 : email, 1번 : kakao로그인 2번 : github }
        user.setSocialKey("");

        if(userService.createUserByEmail(user)){
            return new SuccessDto(true, "signup success");
        }else{
            return new SuccessDto(false, "signup fail");
        }
    }

    @PostMapping("/signup/{social}")
    public ResponseEntity<SuccessDto> createUserBySocial(
            @PathVariable("social") String socialType,
            @RequestBody UserSocialInfoDto userSocialInfoDto){
        String password = "";
        // password validation
        if(!passwordConfig.passwordValidataion(userSocialInfoDto.getPassword())){
            return ResponseEntity.ok().body(new SuccessDto(false, "invalid password error"));
        }

        // email validation
        if(!passwordConfig.emailValidataion(userSocialInfoDto.getEmail())){
            return ResponseEntity.ok().body(new SuccessDto(false, "email invalid error"));
        }

        try{
            password = passwordConfig.hashing(passwordConfig.getSalt() + userSocialInfoDto.getPassword());
        }catch (NoSuchAlgorithmException e){
            return ResponseEntity.ok().body(new SuccessDto(false, "signup fail"));
        }

        User user = new User();
        user.setName(userSocialInfoDto.getName());
        user.setAccount(userSocialInfoDto.getAccount());
        user.setPassword(password);
        user.setEmail(userSocialInfoDto.getEmail());
        user.setAlarm(false);
        user.setImage("");
        user.setNickname("");
        user.setIntroduce("");
        user.setSocialKey(userSocialInfoDto.getSocial_key());

        // sns { 0번 : email, 1번 : kakao로그인, 2번 : github }
        if(socialType.equals("kakao")){
            user.setSns(1);
        }else if(socialType.equals("github")){
            user.setSns(2);
        }else{
            return ResponseEntity.ok().body(new SuccessDto(false, "url error"));
        }

        if(userService.createUserBySocial(user)){
            // 프로필 사진 이미지 저장 추가부분
            Long userId = userService.searchUserId(userSocialInfoDto.getAccount(), userSocialInfoDto.getEmail());
            HttpHeaders headers = new HttpHeaders();
            String accessToken = jwtService.createAccessToken(String.valueOf(userId));
            String refreshToken = jwtService.createRefreshToken(String.valueOf(userId));
            redisService.createJwtAuthToken(new JwtAuthToken(userId, accessToken, refreshToken, null));
            headers.set("userId", String.valueOf(userId));
            headers.set("accessToken", accessToken);
            headers.set("refreshToken", refreshToken);
            return ResponseEntity.ok().headers(headers).body(new SuccessDto(true, "signup success"));
        }else{
            return ResponseEntity.ok().body(new SuccessDto(false, "signup fail"));
        }
    }

    @GetMapping("/my-info")
    public ResponseEntity<SuccessDto> readUser(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken){
        if(jwtService.validateToken(accessToken)){
            String userId = jwtService.getUserPk(accessToken);
            User user = userService.readUser(Long.parseLong(userId));
            Long imageId = userService.findUserImage(user.getImage());
            if(user.getId() != 0){
                UserEmailInfoDto userEmailInfoDto = new UserEmailInfoDto();
                userEmailInfoDto.setName(user.getName());
                userEmailInfoDto.setAccount(user.getAccount());
                userEmailInfoDto.setEmail(user.getEmail());
                userEmailInfoDto.setAlarm(user.getAlarm());
                userEmailInfoDto.setNickname(user.getNickname());
                userEmailInfoDto.setIntroduce(user.getIntroduce());

                if (imageId.equals(0L)) {
                    userEmailInfoDto.setImageId(null);
                    userEmailInfoDto.setImage(null);
                }else{
                    userEmailInfoDto.setImageId(imageId);
                    userEmailInfoDto.setImage(user.getImage());
                }

                return new ResponseEntity<SuccessDto>(new SuccessDto(true, userEmailInfoDto),HttpStatus.OK);
            }else{
                return new ResponseEntity<SuccessDto>(new SuccessDto(false, "user read error"), HttpStatus.OK);
            }
        }else{
            // jwt accessToken 인증시간 초과
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        }
    }

    //memberpk로 유저 정보 검색
    //deleteUser()
    //삭제된 유저 정보 반환
    @GetMapping("/memberout")
    public ResponseEntity<SuccessDto> deleteUser(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken){
        if(jwtService.validateToken(accessToken)){
            String userId = jwtService.getUserPk(accessToken);
            User user = userService.readUser(Long.parseLong(userId));
            if(user.getId() != 0){
                userService.deleteUser(Long.parseLong(userId));
                UserEmailInfoDto userEmailInfoDto = new UserEmailInfoDto();
                userEmailInfoDto.setName(user.getName());
                userEmailInfoDto.setAccount(user.getAccount());
                userEmailInfoDto.setPassword(user.getPassword());
                userEmailInfoDto.setEmail(user.getEmail());
                userEmailInfoDto.setAlarm(user.getAlarm());
                userEmailInfoDto.setImage(user.getImage());
                userEmailInfoDto.setNickname(user.getNickname());
                userEmailInfoDto.setIntroduce(user.getIntroduce());
                return new ResponseEntity<SuccessDto>(new SuccessDto(true, userEmailInfoDto), HttpStatus.OK);
            }else{
                return new ResponseEntity<SuccessDto>(new SuccessDto(false, "user delete error"), HttpStatus.OK);
            }
        }else{
            // jwt accessToken, jwt refreshToken 인증시간 초과
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        }
    }


    @PutMapping("/my-info-update")
    public ResponseEntity<SuccessDto> updateUser(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
            @RequestBody UserEmailInfoDto userEmailInfoDto){
        if(jwtService.validateToken(accessToken)){
            String userId = jwtService.getUserPk(accessToken);

            // password validation
            if(!passwordConfig.passwordValidataion(userEmailInfoDto.getPassword())){
                return ResponseEntity.ok().body(new SuccessDto(false, "invalid password error"));
            }
            // password hashing
            try{
                userEmailInfoDto.setPassword(passwordConfig.hashing(passwordConfig.getSalt() + userEmailInfoDto.getPassword()));
            }catch (NoSuchAlgorithmException e){
                return new ResponseEntity<SuccessDto>(new SuccessDto(false, "update error"), HttpStatus.OK);
            }

            if(userService.updateUser(Long.parseLong(userId), userEmailInfoDto)){
                return new ResponseEntity<SuccessDto>(new SuccessDto(true, "update success"), HttpStatus.OK);
            }else{
                return new ResponseEntity<SuccessDto>(new SuccessDto(false, "update error"), HttpStatus.OK);
            }
        }else{
            // jwt accessToken, jwt refreshToken 인증시간 초과
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "update error"), HttpStatus.valueOf(403));

        }
    }

    @PostMapping("/updatepw")
    public ResponseEntity<SuccessDto> changePassword(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
            @RequestBody UserPwChangeDto userPwChangeDto){
        if(jwtService.validateToken(accessToken)){

            // password validation
            if(!passwordConfig.passwordValidataion(userPwChangeDto.getPassword())){
                return new ResponseEntity<SuccessDto>(new SuccessDto(false, "password invalid error"), HttpStatus.OK);
            }
            // password hashing
            try{
                userPwChangeDto.setPassword(passwordConfig.hashing(passwordConfig.getSalt() + userPwChangeDto.getPassword()));
            }catch (NoSuchAlgorithmException e){
                return new ResponseEntity<SuccessDto>(new SuccessDto(false, "update error"), HttpStatus.OK);
            }

            if(userService.changePassword(userPwChangeDto.getEmail(), userPwChangeDto.getAccount(), userPwChangeDto.getPassword())){
                return new ResponseEntity<SuccessDto>(new SuccessDto(true, "password update success"), HttpStatus.OK);
            }else {
                return new ResponseEntity<SuccessDto>(new SuccessDto(false, "password update error"), HttpStatus.OK);
            }
        }else{
            // jwt accessToken, jwt refreshToken 인증시간 초과
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessDto> login(@RequestBody UserLoginDto userLoginDto){
        try{
            userLoginDto.setPassword(passwordConfig.hashing(passwordConfig.getSalt() + userLoginDto.getPassword()));
        }catch (NoSuchAlgorithmException e){
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "login fail"), HttpStatus.OK);
        }

        User user = userService.login(userLoginDto.getAccount(), userLoginDto.getPassword());
        if(user != null){
            HttpHeaders headers = new HttpHeaders();
            String accessToken = jwtService.createAccessToken(String.valueOf(user.getId()));
            String refreshToken = jwtService.createRefreshToken(String.valueOf(user.getId()));
            redisService.createJwtAuthToken(new JwtAuthToken(user.getId(), accessToken, refreshToken, null));
            headers.set("userId", String.valueOf(user.getId()));
            headers.set("accessToken", accessToken);
            headers.set("refreshToken", refreshToken);
            return ResponseEntity.ok().headers(headers).body(new SuccessDto(true, "login success"));
        }else {
            return ResponseEntity.ok().body(new SuccessDto(false, "login fail"));
        }
    }

    //kakao 인증 url
    //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=ac1a0ec2e424c32d5baa95bf6114d8b0&redirect_uri=http://127.0.0.1:8080/login/kakao&scope=account_email,openid

    //github 인증 url
    //https://github.com/login/oauth/authorize?client_id=0006efe23ef0c6ecb6c0&redirect_uri=http://localhost:8080/login/github
    @PostMapping("/login/{social}")
    public ResponseEntity<SuccessDto> loginBySocial(@PathVariable("social") String socialType, @RequestBody UserCodeDto userCodeDto) {
        String socialAccessToken = "";
        String socialKey = "";
        Integer sns = 0;
        if(socialType.equals("kakao")){
            socialAccessToken = kakaoAuthService.getKaKaoAccessToken(userCodeDto.getCode()); // 인가코드 받기
            socialKey = kakaoAuthService.getKakaoUserKey(socialAccessToken); // accessToken 받기
            sns = 1;
        }else if(socialType.equals("github")) {
            socialAccessToken = githubAuthService.getGithubAccessToken(userCodeDto.getCode()); // 인가코드 받기
            socialKey = githubAuthService.getGithubUserKey(socialAccessToken); // accessToken 받기
            sns = 2;
        }else{
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "social login url error"), HttpStatus.OK);
        }
        User user = userService.searchSocialKey(sns, socialKey);

        if(user != null){ // 로그인 유저 식별 id 확인 후 로그인 또는 회원가입 진행
            // 이미 존재하는 socialKey
            HttpHeaders headers = new HttpHeaders();
            String accessToken = jwtService.createAccessToken(String.valueOf(user.getId()));
            String refreshToken = jwtService.createRefreshToken(String.valueOf(user.getId()));
            redisService.createJwtAuthToken(new JwtAuthToken(user.getId(), accessToken, refreshToken, null));
            headers.set("userId", String.valueOf(user.getId()));
            headers.set("accessToken", accessToken);
            headers.set("refreshToken", refreshToken);
            return ResponseEntity.ok().headers(headers).body(new SuccessDto(false, "social login success"));
        }else{
            // 새로운 socialKey
            return new ResponseEntity<SuccessDto>(new SuccessDto(true, socialKey), HttpStatus.OK);
        }
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
        int emailAuthNumber = emailAuthService.sendMail(userEmailDto.getEmail());
        if(redisService.createEmailAuthNumber(userEmailDto.getEmail(), String.valueOf(emailAuthNumber))){
            return new SuccessDto(true, "mail send success");
        }else{
            return new SuccessDto(false, "mail send error");
        }
    }

    @PostMapping("/email/auth")
    public SuccessDto emailAuth(@RequestBody UserEmailAuthNumber userEmailAuthNumber){
        String emailAuthNumber = String.valueOf(redisService.readEmailAuthNumber(userEmailAuthNumber.getEmail()));
        if (emailAuthNumber.equals(userEmailAuthNumber.getEmailAuthNumber())) {
            return new SuccessDto(true, "email auth success");
        }else{
            return new SuccessDto(false, "email auth error");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessDto> logoutSocial(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken){
        if(jwtService.validateToken(accessToken)){
            String useId = jwtService.getUserPk(accessToken);
            if(redisService.deleteJwtAuthToken(Long.parseLong(useId))){
                return new ResponseEntity<SuccessDto>(new SuccessDto(true, "logout success"), HttpStatus.OK);
            }else{
                return new ResponseEntity<SuccessDto>(new SuccessDto(false, "session error"), HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "logout error"), HttpStatus.valueOf(403));
        }
    }

    @PostMapping("auth/refresh-token")
    public ResponseEntity<SuccessDto> reissueAccessToken(@RequestHeader(value = "X-AUTH-TOKEN") String refreshToken){
        if(jwtService.validateToken(refreshToken)){
            HttpHeaders headers = new HttpHeaders();
            String userId = jwtService.getUserPk(refreshToken);
            String reissuedAccessToken = jwtService.createAccessToken(userId);
            redisService.createJwtAuthToken(new JwtAuthToken(Long.parseLong(userId), reissuedAccessToken, refreshToken, null));
            headers.set("X-AUTH-TOKEN", reissuedAccessToken);
            return new ResponseEntity<SuccessDto>(new SuccessDto(true, "reissue success"), headers, HttpStatus.OK);
        }else{
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "reissue error"), HttpStatus.valueOf(403));
        }
    }
}
