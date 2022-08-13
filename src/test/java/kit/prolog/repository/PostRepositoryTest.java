package kit.prolog.repository;

import kit.prolog.domain.Like;
import kit.prolog.domain.Post;
import kit.prolog.domain.User;
import kit.prolog.repository.jpa.LikeRepository;
import kit.prolog.repository.jpa.MoldRepository;
import kit.prolog.repository.jpa.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostRepositoryTest {
    @Autowired private PostRepository postRepository;
    @Autowired private LikeRepository likeRepository;
    @Autowired private MoldRepository moldRepository;

    @Test
    void 카테고리별_게시글_조회(){
        String account = "sky834459";
        String categoryName = "개발용";
        int cursor = 0;
        postRepository.findPostByCategoryName(account, categoryName, cursor)
                .forEach(System.out::println);

    }

    @Test
    void 게시글_좋아요_취소(){
        Long userId = 1L, postId = 7L;
        Optional<Like> like = likeRepository.findByUser_IdAndPost_Id(userId, postId);
        if(like.isPresent()){
            likeRepository.delete(like.get());
            System.out.println("좋아요 취소");
        }else{
            // 예외처리 주의
            likeRepository.save(new Like(new User(userId), new Post(postId)));
            System.out.println("좋아요 등록");
        }
    }

    @Test
    void 게시글_레이아웃틀_비우기(){
        //given
        Long moldId = 1L;
        List<Post> posts = postRepository.findByMold_Id(moldId);
        //when
        posts.forEach(post -> {
            post.setMold(null);
            postRepository.saveAndFlush(post);
        });
        posts.forEach(post -> System.out.println(post.getMold()));

        assertThat(posts)
                .filteredOn(post -> post.getMold() == null)
                .hasSize(posts.size());
    }
}
