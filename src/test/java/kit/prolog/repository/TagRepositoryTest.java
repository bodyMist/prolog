package kit.prolog.repository;

import kit.prolog.domain.Post;
import kit.prolog.domain.Tag;
import kit.prolog.repository.jpa.PostRepository;
import kit.prolog.repository.jpa.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TagRepositoryTest {
    @Autowired TagRepository tagRepository;
    @Autowired PostRepository postRepository;

    @Test
    void 중복태그_저장_테스트(){
        Tag tag = new Tag();
//        tag.setName("react");
        System.out.println(tagRepository.save(tag));
        // Duplicate Unique Key Error -> 게시글 작성/수정 시 태그의 경우 exist를 먼저 거치고 save 필요
    }

    @Test
    void 태그_검색_없음(){
        assertThat(tagRepository.findByName("아가야").isPresent()).isEqualTo(false);
    }
}
