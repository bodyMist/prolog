package kit.prolog.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserEmailInfoDto {
    private String name;
    private String account;
    private String password;
    private String email;
    private boolean alarm;
    private String image;
    private String nickname;
    private String introduce;
}
