package kit.prolog.service;

import kit.prolog.domain.*;
import kit.prolog.dto.AttachmentDto;
import kit.prolog.dto.CommentLv1Dto;
import kit.prolog.dto.LayoutDto;
import kit.prolog.dto.PostDetailDto;
import kit.prolog.repository.jpa.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks private PostService postService;

    @Mock private PostRepository postRepository;
    @Mock private MoldRepository moldRepository;
    @Mock private LayoutRepository layoutRepository;
    @Mock private UserRepository userRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private AttachmentRepository attachmentRepository;
    @Mock private TagRepository tagRepository;
    @Mock private PostTagRepository postTagRepository;
    @Mock private LikeRepository likeRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private HitRepository hitRepository;


    @Test
    void 게시글_작성(){
        // given
        Long userId = 1L, moldId = 1L, categoryId = 1L;
        String title = "게시글 제목";
        List<LayoutDto> layoutList = List.of(new LayoutDto(1L, "콘텐트"), new LayoutDto(2L, "http://"));
        HashMap<String, Object> param = new HashMap<>();

        List<AttachmentDto> attachmentList = List.of(new AttachmentDto(1L, "url"));
        param.put("attachments", attachmentList);
        param.put("tags", List.of("react", "spring"));

        Post post = new Post(title, LocalDateTime.now(), new User(userId),
                Category.builder().id(categoryId).build(), new Mold(moldId));

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(moldRepository.findById(moldId)).thenReturn(Optional.of(new Mold(moldId)));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(Category.builder().id(categoryId).build()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(userId)));

        Long writePost = postService.writePost(userId, moldId, title, layoutList, categoryId, param);

        assertThat(writePost).isEqualTo(post.getId());
        verify(tagRepository, times(2)).save(any(Tag.class));
        verify(attachmentRepository, times(1)).save(any(Attachment.class));
    }

    @Test
    void 게시글_조회(){
        //given
        Long userId = 1L, postId = 1L;
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time"));

        when(postRepository.findPostById(postId)).thenReturn(any(PostDetailDto.class));
        when(likeRepository.existsByUser_IdAndPost_Id(any(), any())).thenReturn(true);
        when(likeRepository.countByPost_Id(postId)).thenReturn(any(int.class));
        when(commentRepository.findByPostId(postId, userId, pageable)).thenReturn(List.of(new CommentLv1Dto()));

        PostDetailDto postDetailDto = postService.viewPostDetailById(userId, postId);
        assertThat(postDetailDto).isNotNull();
    }

    @Test
    void 게시글_삭제_연쇄작용(){
        Long postId = 1L;

        when(likeRepository.deleteAllByPost_Id(postId)).thenReturn(true);
        when(commentRepository.deleteAllByPost_Id(postId)).thenReturn(true);
        when(hitRepository.deleteAllByPost_Id(postId)).thenReturn(true);
        when(postTagRepository.deleteAllByPost_Id(postId)).thenReturn(true);
        when(attachmentRepository.deleteAllByPost_Id(postId)).thenReturn(true);

        when(postRepository.findMoldIdByPostId(postId)).thenReturn(1L);
        when(layoutRepository.deleteAllByMold_Id(postId)).thenReturn(true);
        Mockito.doNothing().when(postRepository).deleteById(postId);

        boolean deletePost = postService.deletePost(postId);

        assertThat(deletePost).isTrue();
    }
}
