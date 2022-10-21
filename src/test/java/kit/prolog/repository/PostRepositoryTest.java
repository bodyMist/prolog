package kit.prolog.repository;


import kit.prolog.config.EmailConfig;
import kit.prolog.config.QuerydslConfig;
import kit.prolog.domain.*;
import kit.prolog.repository.jpa.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QuerydslConfig.class, EmailConfig.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:test.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private MoldRepository moldRepository;
    @Autowired private LayoutRepository layoutRepository;
    @Autowired private ContextRepository contextRepository;

    @BeforeEach
    public void setUp(){
        User user = new User();
        user.setAccount("asdf111");
        user.setPassword("asdf12!");
        user.setEmail("tkdrms0301@naver.com");
        user.setSns(0);
        user.setName("안상근");
        user.setImage("");
        user.setNickname("An");
        user.setIntroduce("hello world!");
        user.setAlarm(true);
        userRepository.save(user);
    }

    @Test
    @DisplayName("틀 없이 게시글 저장")
    public void savePostWithNoMold(){
        // given
        String userEmail = "tkdrms0301@naver.com";
        User user = userRepository.findOneByEmail(userEmail);
        Optional<Category> category = categoryRepository.findById(1L);
        Post post = new Post("test-title", LocalDateTime.now(), user, category.get());
        Layout layout = layoutRepository.getById(1L);
        Context context = new Context(true, post, layout);

        // when
        Post savedPost = postRepository.save(post);
        Layout updatedLayout = layoutRepository.save(layout);
        Context savedContext = contextRepository.save(context);

        // then
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(savedPost.getUser().getId()).isEqualTo(user.getId());

        assertThat(updatedLayout.getCoordinateX()).isEqualTo(layout.getCoordinateX());

        assertThat(savedPost.getId()).isEqualTo(savedContext.getPost().getId());
    }

    @Test
    @DisplayName("틀 사용 게시글 저장")
    public void savePostWithMold(){
        // given
        User user = userRepository.findOneById(1L);
        Optional<Category> category = categoryRepository.findById(1L);
        Mold mold = moldRepository.getById(1L);
        Post post = new Post("test-title", LocalDateTime.now(), user, category.get());
        post.setMold(mold);
        Layout layout = layoutRepository.getById(1L);
        layout.setExplanation("테스트용가리");
        layout.setWidth(100.0);
        layout.setHeight(100.0);
        layout.setCoordinateX(100.0);
        layout.setCoordinateY(100.0);
        Context context = new Context(true, post, layout);

        // when
        Post savedPost = postRepository.save(post);
        Layout updatedLayout = layoutRepository.save(layout);
        Context savedContext = contextRepository.save(context);

        // then
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getMold()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(savedPost.getUser().getId()).isEqualTo(user.getId());

        assertThat(updatedLayout.getExplanation()).isEqualTo("테스트용가리");

        assertThat(savedPost.getId()).isEqualTo(savedContext.getPost().getId());
    }
}
