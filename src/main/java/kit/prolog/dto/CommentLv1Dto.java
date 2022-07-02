package kit.prolog.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CommentLv1Dto extends CommentLv2Dto {
    private List<CommentLv2Dto> lowerComments;
}
