package kit.prolog.repository.jpa;

import kit.prolog.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUser_IdAndPost_Id(Long userId, Long postId);

    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);
    int countByPost_Id(Long postId);
}
