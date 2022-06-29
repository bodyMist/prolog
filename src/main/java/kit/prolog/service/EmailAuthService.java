package kit.prolog.service;

import kit.prolog.repository.jpa.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class EmailAuthService {
    private final UserRepository userRepository;

    // email 발송
    public void sendEmail(){

    }

    // email 인증
    public boolean emailAuth(int emailAuthNumber){

        return true;
    }
}
