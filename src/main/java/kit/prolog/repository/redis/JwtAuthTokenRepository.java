package kit.prolog.repository.redis;

import kit.prolog.domain.redis.JwtAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtAuthTokenRepository extends JpaRepository<JwtAuthToken, Long> {
    JwtAuthToken findOneById(Long id);
}
