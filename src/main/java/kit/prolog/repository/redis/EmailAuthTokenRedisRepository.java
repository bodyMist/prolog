package kit.prolog.repository.redis;

import kit.prolog.domain.redis.EmailAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface EmailAuthTokenRedisRepository extends CrudRepository<EmailAuthToken, String> {
    EmailAuthToken findOneByEmail(String email);
}
