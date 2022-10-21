package kit.prolog.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class UserLoginDto {
    private String account;
    private String password;
}
