package kit.prolog.repository.custom;

import kit.prolog.dto.LayoutDto;
import kit.prolog.dto.PostDetailDto;
import kit.prolog.dto.PostPreviewDto;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostCustomRepository {
    int PAGE_SIZE = 3;
    PostDetailDto findPostById(Long id);
    List<LayoutDto> selectDetailLayout(Long postId);
    List<PostPreviewDto> findPostByCategoryName(String account, String categoryName, int cursor);
    List<PostPreviewDto> findMyPostByUserId(Long userId, int cursor);
    List<PostPreviewDto> findLikePostByUserId(Long userId, int cursor);
    List<PostPreviewDto> findAllPost(int cursor);
}
