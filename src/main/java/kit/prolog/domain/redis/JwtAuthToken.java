package kit.prolog.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash("jwtAuthToken")
@AllArgsConstructor
public class JwtAuthToken {
    @Id @Indexed
    private Long id;
    @Indexed
    private String accessToken;
    @Indexed
    private String refreshToken;
    @TimeToLive
    private Long expiration;
}
