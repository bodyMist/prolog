package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserLoginDto {
    private String account;
    private String password;
}
