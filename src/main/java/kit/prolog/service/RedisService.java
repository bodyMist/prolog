package kit.prolog.service;

import kit.prolog.domain.redis.EmailAuthToken;
import kit.prolog.repository.redis.EmailAuthTokenRedisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class RedisService {
    @Autowired
    private EmailAuthTokenRedisRepository emailAuthTokenRedisRepository;

    public boolean createEmailAuthNumber(String email, int emailAuthNumber){
        try{
            EmailAuthToken emailAuthToken = emailAuthTokenRedisRepository.findOneByEmail(email);
            if(emailAuthToken != null){
                emailAuthToken.setEmailAuthNumber(emailAuthNumber);
                emailAuthToken.setExpiration(180L);
                emailAuthTokenRedisRepository.save(emailAuthToken);
            }else{
                emailAuthTokenRedisRepository.save(new EmailAuthToken(email, emailAuthNumber, 180L));
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
            e.printStackTrace();
            return 0;
        }
    }


}
