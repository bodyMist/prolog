package kit.prolog.repository.redis;

import kit.prolog.domain.redis.EmailAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAuthTokenRedisRepository extends JpaRepository<EmailAuthToken, String> {
    EmailAuthToken findOneByEmail(String email);
}
