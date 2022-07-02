package kit.prolog.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
public class CommentLv2Dto {
    private Long id;
    private Long userId;
    private String nickname;
    private String userImage;
    private Boolean isPostWriter;
    private Boolean isCommentWriter;
    private String context;
    private LocalDateTime time;
}
