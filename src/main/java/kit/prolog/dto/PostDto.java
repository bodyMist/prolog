package kit.prolog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

/*
* PostDetailDto를 위한 부분 DTO
* */
@ToString
@Getter
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    @JsonProperty("written")
    private LocalDate time;
}
