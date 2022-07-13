package kit.prolog.repository.jpa;

import kit.prolog.domain.PostTag;
import kit.prolog.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findByPost_Id(Long postId);
}
