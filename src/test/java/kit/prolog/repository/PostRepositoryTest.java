package kit.prolog.repository;

import kit.prolog.domain.Like;
import kit.prolog.domain.Post;
import kit.prolog.domain.User;
import kit.prolog.repository.jpa.LikeRepository;
import kit.prolog.repository.jpa.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class PostRepositoryTest {
    @Autowired private PostRepository postRepository;
    @Autowired private LikeRepository likeRepository;

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
}
