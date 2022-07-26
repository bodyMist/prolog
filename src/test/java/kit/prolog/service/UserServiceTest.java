package kit.prolog.service;


import kit.prolog.domain.Post;
import kit.prolog.domain.User;
import kit.prolog.repository.jpa.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock private PostRepository postRepository;
    @Mock private MoldRepository moldRepository;
    @Mock private LayoutRepository layoutRepository;
    @Mock private UserRepository userRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private AttachmentRepository attachmentRepository;
    @Mock private PostTagRepository postTagRepository;
    @Mock private LikeRepository likeRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private HitRepository hitRepository;

//    @BeforeEach
    void setUp(){
        Long userId = 1L, postId = 1L, moldId = 1L;
        User user = new User(userId);
        Post post = new Post(postId);

        when(likeRepository.deleteAllByUser_Id(userId)).thenReturn(true);
        when(postRepository.findByUser_Id(userId)).thenReturn(List.of(post));

        when(hitRepository.deleteAllByPost_Id(postId)).thenReturn(true);
        when(commentRepository.deleteAllByPost_Id(postId)).thenReturn(true);
        when(attachmentRepository.deleteAllByPost_Id(postId)).thenReturn(true);
        when(postTagRepository.deleteAllByPost_Id(postId)).thenReturn(true);
        when(postRepository.findMoldIdByPostId(postId)).thenReturn(moldId);
        when(layoutRepository.deleteAllByMold_Id(moldId)).thenReturn(true);

        Mockito.doNothing().when(postRepository).deleteById(postId);
        Mockito.doNothing().when(moldRepository).deleteById(moldId);

        when(commentRepository.blockCommentsByUserId(userId)).thenReturn(true);
        when(categoryRepository.deleteAllByUser_Id(userId)).thenReturn(true);

        when(userRepository.findOneById(userId)).thenReturn(user);
//        Mockito.doNothing().when(userRepository).deleteById(userId);

    }

    @Test
    void 회원탈퇴() {
        Long userId = 1L;
        User user = new User(userId);

        User result = userService.deleteUser(userId);
        assertThat(result).isEqualTo(user);
    }

    @Test
    void 이메일회원가입(){
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
        System.out.println(userService.createUserByEmail(user));
    }

    @Test
    void 소셜회원가입(){
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
        System.out.println(userService.createUserBySocial(user));
    }

    @Test
    void 회원조회(){
        User user = new User(1L);
        user.setAccount("asdf111");
        user.setPassword("asdf12!");
        user.setEmail("tkdrms0301@naver.com");
        user.setSns(0);
        user.setName("안상근");
        user.setImage("");
        user.setNickname("An");
        user.setIntroduce("hello world!");
        user.setAlarm(true);
        when(userService.readUser(1L)).thenReturn(user);
        System.out.println(userService.readUser(1L).getName());
    }

    @Test
    void 로그인(){
        String account = "asdf111";
        String password = "asdf12!";
        User user = new User(1L);
        user.setAccount("asdf111");
        user.setPassword("asdf12!");
        user.setEmail("tkdrms0301@naver.com");
        user.setSns(0);
        user.setName("안상근");
        user.setImage("");
        user.setNickname("An");
        user.setIntroduce("hello world!");
        user.setAlarm(true);
        when(userRepository.findByAccountAndPassword(account, password)).thenReturn(user);
        System.out.println(userService.login("asdf111", "asdf12!"));
    }

    @Test
    void 아이디찾기(){
        String email = "tkdrms0301@naver.com";
        User user = new User(1L);
        user.setAccount("asdf111");
        user.setPassword("asdf12!");
        user.setEmail("tkdrms0301@naver.com");
        user.setSns(0);
        user.setName("안상근");
        user.setImage("");
        user.setNickname("An");
        user.setIntroduce("hello world!");
        user.setAlarm(true);
        when(userRepository.findOneByEmail(email)).thenReturn(user);
        System.out.println(userService.searchAccount(email));
    }

    @Test
    void 회원정보수정(){
        User user = new User(1L);
        user.setAccount("asdf111");
        user.setPassword("asdf12!");
        user.setEmail("tkdrms0301@naver.com");
        user.setSns(0);
        user.setName("안상근");
        user.setImage("");
        user.setNickname("An");
        user.setIntroduce("hello world!");
        user.setAlarm(true);
        when(userService.updateUser(1L, user)).thenReturn(user);
        System.out.println(userService.updateUser(1L, user));
    }

    @Test
    void 비밀번호변경(){
        String account = "asdf111";
        String password = "asdf12!";
        User user = new User(1L);
        user.setAccount("asdf111");
        user.setPassword("asdf12!");
        user.setEmail("tkdrms0301@naver.com");
        user.setSns(0);
        user.setName("안상근");
        user.setImage("");
        user.setNickname("An");
        user.setIntroduce("hello world!");
        user.setAlarm(true);
        when(userRepository.findByAccountAndPassword(account, password)).thenReturn(user);
        System.out.println(userService.changePassword(account, password));
    }
}
