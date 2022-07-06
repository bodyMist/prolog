package kit.prolog.service;

import kit.prolog.dto.LayoutDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Transactional
public class PostServiceTest {
    @Autowired private PostService postService;

    @Test
    void 레이아웃_리스트_저장(){
        Long userId = 1L;
        String moldName = "기본틀";
        List<LayoutDto> layoutList = List.of(new LayoutDto(1, 1.0, 1.0, 1, 1));
        postService.saveLayouts(userId, moldName, layoutList);
    }

    @Test
    void 게시글_작성(){
        Long userId = 1L, moldId = 1L, categoryId = 1L;
        String title = "씨발 존나 하기 싫어";
        List<LayoutDto> layoutList = List.of(new LayoutDto(1L, "콘텐트"), new LayoutDto(2L, "http://"));
        postService.writePost(userId, moldId, title, layoutList, categoryId);
    }

    @Test
    void 레이아웃틀_cascade(){
        Long moldId = 1L;
        postService.deleteMold(moldId);
    }
}
