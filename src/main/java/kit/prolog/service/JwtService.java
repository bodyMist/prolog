package kit.prolog.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kit.prolog.config.JwtConfig;
import kit.prolog.enums.JwtTokenValidType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtService {
    @Autowired
    JwtConfig jwtConfig;

    private final UserDetailsService userDetailsService;

    // JWT 토큰 생성
    private String createToken(String userId, long time) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId); // JWT payload 에 저장되는 정보단위, key value 쌍으로 추가 정보 저장 가능
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + time)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // access token 생성
    public String createAccessToken(String userId){
        return createToken(userId, JwtTokenValidType.ACCESS_TOKEN_EXPIRATION_TIME.getTime());
    }

    // refresh token 생성
    public String createRefreshToken(String userId){
        return createToken(userId, JwtTokenValidType.REFRESH_TOKEN_EXPIRATION_TIME.getTime());
    }

    // reissue token 생성
    public String createReissueToken(String userId){
        return createToken(userId, JwtTokenValidType.REISSUE_EXPIRATION_TIME.getTime());
    }

    // 토큰에서 정보 추출
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
    }

    // 토큰에서 회원 id 추출
    public String getUserPk(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    /*
    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
     */

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 남은 시간 계산
    public long getRemainMilliSeconds(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        Date now = new Date();
        return (expiration.getTime() - now.getTime()) / 1000L;
    }

    public Authentication getAuthentication(String token) {
        String userId = getUserPk(token);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        UserDetails principal = new User(userId, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }
}
