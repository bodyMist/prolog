package kit.prolog.domain.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class EmailAuthToken {
    @Id
    private String email;
    @Indexed
    private int emailAuthNumber;
    @TimeToLive
    private Long expiration;

    public static EmailAuthToken of(String email, int emailAuthNumber, Long expiration){
        return EmailAuthToken.builder()
                .email(email)
                .emailAuthNumber(emailAuthNumber)
                .expiration(expiration / 1000)
                .build();
    }
}
