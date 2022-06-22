package kit.prolog.repository.custom;

import kit.prolog.dto.PostDetailDto;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCustomRepository {
    PostDetailDto findPostById(Long id);
}
