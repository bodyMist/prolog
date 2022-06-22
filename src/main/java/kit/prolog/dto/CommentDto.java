package kit.prolog.dto;

import java.time.LocalDateTime;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
public class CommentDto {
    private Long id;
    private String context;
    private LocalDateTime time;
    private Long upperId;
    private boolean isBlocked;
}
