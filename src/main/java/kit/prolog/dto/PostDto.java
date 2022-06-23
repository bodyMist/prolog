package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/*
* PostDetailDto를 위한 부분 DTO
* */
@ToString
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private LocalDate time;
}
