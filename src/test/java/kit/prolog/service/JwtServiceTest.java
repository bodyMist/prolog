package kit.prolog.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtServiceTest {
    @Autowired
    JwtService jwtService;

    @Test
    void JWT생성테스트(){
        int memberpk = 1;
        System.out.println(jwtService.createAccessToken(String.valueOf(memberpk)));
    }

    @Test
    void JWT인증테스트(){
        //JWT생성테스트 에서 생성한 토큰
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxIiwiaWF0IjoxNjU4OTc0ODMwLCJleHAiOjE2NTg5NzY2MzB9.GoC7WAzyT2POleiUY_xrNmBL6M7RnmiCADsjB1eyaz0";
        System.out.println(jwtService.getUserPk(token));
    }

    @Test
    void JWT유효성테스트(){
        //JWT생성테스트에서 생성한 토큰이후 30분 동안 유효
        //JWT 토큰 2022-07-28 10:36분 생성
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxIiwiaWF0IjoxNjU4OTc0ODMwLCJleHAiOjE2NTg5NzY2MzB9.GoC7WAzyT2POleiUY_xrNmBL6M7RnmiCADsjB1eyaz0";
        //false
        System.out.println(jwtService.validateToken(token));
    }

    @Test
    void JWT남은시간테스트(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxIiwiaWF0IjoxNjU4OTc0ODMwLCJleHAiOjE2NTg5NzY2MzB9.GoC7WAzyT2POleiUY_xrNmBL6M7RnmiCADsjB1eyaz0";
        System.out.println(jwtService.getRemainMilliSeconds(token) + "초");
    }
}
