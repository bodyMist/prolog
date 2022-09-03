package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialCodeDto {
    private String code;
    private String account;
    private String password;
    private String name;
    private String email;
}
