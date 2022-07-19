package kit.prolog.repository.jpa;

import kit.prolog.domain.Comment;
import kit.prolog.repository.custom.CommentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
    boolean deleteAllByPost_Id(Long postId);
}
