package kit.prolog.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailAuthNumber {
    private String email;
    private String emailAuthNumber;
}
