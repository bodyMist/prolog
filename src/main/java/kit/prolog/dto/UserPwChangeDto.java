package kit.prolog.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserPwChangeDto {
    private String account;
    private String password;
    private String email;
}
