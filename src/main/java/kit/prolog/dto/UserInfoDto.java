package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserInfoDto {
    private String name;
    private String account;
    private String password;
    private String email;
    private boolean alarm;
    private String image;
    private String nickname;
    private String introduction;
}
