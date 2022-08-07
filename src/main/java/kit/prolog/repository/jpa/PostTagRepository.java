package kit.prolog.repository.jpa;

import kit.prolog.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @Query("SELECT t.name FROM POSTS_AND_TAGS pt INNER JOIN TAGS t ON pt.tag.id = t.id WHERE pt.post.id = :postId")
    List<String> findTagNameByPost_Id(Long postId);
    boolean deleteAllByPost_Id(Long postId);
}
