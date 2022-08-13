package kit.prolog.repository.jpa;

import kit.prolog.domain.Comment;
import kit.prolog.repository.custom.CommentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
    void deleteAllByPost_Id(Long postId);

    @Query("UPDATE COMMENTS c SET c.block = 1, c.user = null WHERE c.user.id = :userId")
    boolean blockCommentsByUserId(Long userId);
}
