package kit.prolog.repository.jpa;

import kit.prolog.domain.Hit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HitRepository extends JpaRepository<Hit, Long> {
    int countByPost_Id(Long postId);
    boolean deleteAllByPost_Id(Long postId);
}
