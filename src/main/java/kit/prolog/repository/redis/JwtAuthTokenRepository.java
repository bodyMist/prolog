package kit.prolog.repository.redis;

import kit.prolog.domain.redis.JwtAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface JwtAuthTokenRepository extends CrudRepository<JwtAuthToken, Long> {
    JwtAuthToken findOneById(Long id);
}
