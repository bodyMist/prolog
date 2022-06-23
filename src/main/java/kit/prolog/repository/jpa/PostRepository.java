package kit.prolog.repository.jpa;

import kit.prolog.domain.Post;
import kit.prolog.repository.custom.PostCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

}
