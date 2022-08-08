package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserPwChangeDto {
    private String account;
    private String password;
    private String email;
}
