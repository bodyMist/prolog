package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserSocialInfoDto {
    private String account;
    private String email;
    private String name;
    private String password;
    private String social_key;
}
