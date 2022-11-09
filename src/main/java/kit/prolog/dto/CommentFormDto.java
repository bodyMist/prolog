package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentFormDto {
    private Long postId;
    private Long upperCommentId;
    private String context;
}
