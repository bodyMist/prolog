package kit.prolog.repository.custom;

import kit.prolog.dto.PostDetailDto;
import kit.prolog.dto.PostPreviewDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCustomRepository {
    int PAGE_SIZE = 12;
    PostDetailDto findPostById(Long id);
    List<PostPreviewDto> findPostByCategoryName(String account, String categoryName, int cursor);
}
