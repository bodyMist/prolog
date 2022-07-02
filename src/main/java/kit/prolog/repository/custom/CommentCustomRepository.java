package kit.prolog.repository.custom;

import kit.prolog.dto.CommentLv1Dto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentCustomRepository {
    List<CommentLv1Dto> findByPostId(Long postId, Long userId, Pageable pageable);
}
