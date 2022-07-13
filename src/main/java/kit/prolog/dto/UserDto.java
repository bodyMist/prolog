package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
@Getter
@ToString
@AllArgsConstructor
public class UserDto {
    private String name;
    private String image;
}
