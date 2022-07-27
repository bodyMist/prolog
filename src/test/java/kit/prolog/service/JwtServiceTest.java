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
        System.out.println(jwtService.createToken(String.valueOf(memberpk)));
    }

    @Test
    void JWT인증테스트(){
        //JWT생성테스트 에서 생성한 토큰
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU4ODg3NTQ5LCJleHAiOjE2NTg4ODkzNDl9.BeXLvCg34I1SxA6byzoi_BKGQG3wolJ7ZW5P2Vg_8A4";
        System.out.println(jwtService.getUserPk(token));
    }

    @Test
    void JWT유효성테스트(){
        //JWT생성테스트에서 생성한 토큰이후 30분 동안 유효
        //JWT 토큰 2022-07-26 11:30분 생성
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU4ODg3NTQ5LCJleHAiOjE2NTg4ODkzNDl9.BeXLvCg34I1SxA6byzoi_BKGQG3wolJ7ZW5P2Vg_8A4";

        //false
        System.out.println(jwtService.validateToken(token));
    }
}
