package kit.prolog.repository;

import kit.prolog.repository.jpa.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PostRepositoryTest {
    @Autowired private PostRepository postRepository;

    @Test
    void 카테고리별_게시글_조회(){
        String account = "sky834459";
        String categoryName = "개발용";
        int cursor = 0;
        postRepository.findPostByCategoryName(account, categoryName, cursor)
                .forEach(System.out::println);

    }
}
