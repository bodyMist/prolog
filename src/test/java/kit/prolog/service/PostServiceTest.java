package kit.prolog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class PostServiceTest {
    @Autowired private PostService postService;

    @Test
    void 레이아웃틀_cascade(){
        Long moldId = 1L;
        postService.deleteMold(moldId);
    }
}
