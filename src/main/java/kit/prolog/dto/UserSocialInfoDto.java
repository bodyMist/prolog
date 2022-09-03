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
    private String name;
    private String account;
    private String password;
    private String email;
}
