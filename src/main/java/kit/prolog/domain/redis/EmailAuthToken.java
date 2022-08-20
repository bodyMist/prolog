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
@RedisHash("emailAuthToken")
@AllArgsConstructor
public class EmailAuthToken {
    @Id @Indexed
    private String email;
    @Indexed
    private int emailAuthNumber;
    @TimeToLive
    private Long expiration;
}
