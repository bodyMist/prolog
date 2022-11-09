package kit.prolog.repository.jpa;

import kit.prolog.domain.Category;
import kit.prolog.domain.Post;
import kit.prolog.repository.custom.PostCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    @Query("SELECT p, m FROM POSTS p INNER JOIN MOLDS m ON p.mold.id = m.id WHERE m.id = :moldId")
    List<Post> findByMold_Id(@Param("moldId") Long moldId);

    @Query("SELECT p.mold.id FROM POSTS p WHERE p.id = :id")
    Long findMoldIdByPostId(@Param("id") Long id);

    List<Post> findByUser_Id(Long userId);

    @Modifying
    @Query("UPDATE POSTS p SET p.category = :newCategory WHERE p.category = :oldCategory")
    int updatePostCategory(@Param("oldCategory") Category oldCategory,@Param("newCategory") Category newCategory);
}
