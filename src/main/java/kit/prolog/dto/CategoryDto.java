package kit.prolog.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
@Getter
@SuperBuilder
public class CategoryDto {
    private Long id;
    private String name;
}
