package kit.prolog.service;

import kit.prolog.domain.redis.EmailAuthToken;
import kit.prolog.domain.redis.JwtAuthToken;
import kit.prolog.repository.redis.EmailAuthTokenRedisRepository;
import kit.prolog.repository.redis.JwtAuthTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kit.prolog.enums.JwtTokenValidType.REFRESH_TOKEN_EXPIRATION_TIME;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class RedisService {
    @Autowired
    private EmailAuthTokenRedisRepository emailAuthTokenRedisRepository;
    @Autowired
    private JwtAuthTokenRepository jwtAuthTokenRepository;

    public boolean createEmailAuthNumber(String email, String emailAuthNumber){
        try{
            EmailAuthToken emailAuthToken = emailAuthTokenRedisRepository.findOneByEmail(email);
            if(emailAuthToken != null){
                emailAuthToken.setEmailAuthNumber(Integer.parseInt(emailAuthNumber));
                emailAuthToken.setExpiration(180L);
                emailAuthTokenRedisRepository.save(emailAuthToken);
            }else{
                emailAuthTokenRedisRepository.save(new EmailAuthToken(email, Integer.parseInt(emailAuthNumber), 180L));
            }
            return true;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public int readEmailAuthNumber(String email){
        try{
            EmailAuthToken emailAuthToken = emailAuthTokenRedisRepository.findOneByEmail(email);
            if(emailAuthToken != null){
                return emailAuthToken.getEmailAuthNumber();
            }else{
                return 0;
            }
        }catch (NullPointerException e) {
            return 0;
        }
    }

    public boolean createJwtAuthToken(JwtAuthToken newJwtAuthToken){
        try{
            JwtAuthToken jwtAuthToken = jwtAuthTokenRepository.findOneById(newJwtAuthToken.getId());
            if(jwtAuthToken != null){
                jwtAuthToken.setAccessToken(newJwtAuthToken.getAccessToken());
                jwtAuthToken.setRefreshToken(newJwtAuthToken.getRefreshToken());
                jwtAuthToken.setExpiration(REFRESH_TOKEN_EXPIRATION_TIME.getTime());
            }else{
                jwtAuthTokenRepository.save(newJwtAuthToken);
            }
            return true;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public JwtAuthToken readJwtAuthToken(Long userId){
        try{
            JwtAuthToken jwtAuthToken = jwtAuthTokenRepository.findOneById(userId);
            if(jwtAuthToken != null){
                return jwtAuthToken;
            }else{
                return null;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteJwtAuthToken(Long userId){
        try{
            JwtAuthToken jwtAuthToken = jwtAuthTokenRepository.findOneById(userId);
            if(jwtAuthToken != null){
                jwtAuthTokenRepository.deleteById(userId);
                return true;
            }else{
                return false;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
}
