package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentFormDto {
    private Long postId;
    private Long upperCommentId;
    private String context;
}
