package kit.prolog.service;

import kit.prolog.domain.Comment;
import kit.prolog.domain.Post;
import kit.prolog.domain.User;
import kit.prolog.dto.CommentFormDto;
import kit.prolog.repository.jpa.CommentRepository;
import kit.prolog.repository.jpa.PostRepository;
import kit.prolog.repository.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User(1L)));

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(new Post(1L)));
        when(postRepository.findById(2L))
                .thenReturn(Optional.empty());

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(userRepository.findById(1L).get());
        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));
        when(commentRepository.findById(2L))
                .thenReturn(Optional.empty());
    }

    @Test
    void 댓글_생성_성공() {
        Long userId = 1L;
        CommentFormDto commentFormDto = new CommentFormDto(1L, null, "댓글");

        commentService.insertComment(commentFormDto, userId);

        Comment comment = Comment.builder()
                .user(userRepository.findById(userId).get())
                .post(postRepository.findById(commentFormDto.getPostId()).get())
                .context(commentFormDto.getContext())
                .build();
        verify(commentRepository, times(1))
                .save(eq(comment));
    }

    @Test
    void 대댓글_생성_성공() {
        Long userId = 1L;
        Long upperCommentId = 1L;
        CommentFormDto commentFormDto = new CommentFormDto(1L, upperCommentId, "대댓글");

        commentService.insertComment(commentFormDto, userId);

        Comment comment = Comment.builder()
                .user(userRepository.findById(userId).get())
                .post(postRepository.findById(commentFormDto.getPostId()).get())
                .upperComment(commentRepository.findById(upperCommentId).get())
                .context(commentFormDto.getContext())
                .build();
        verify(commentRepository, times(1))
                .save(eq(comment));
    }

    @Test
    void 없는_게시글에_댓글_생성() {
        Long userId = 1L;
        CommentFormDto commentFormDto = new CommentFormDto(2L, null, "댓글");

        assertThrows(NoSuchElementException.class,
                () -> commentService.insertComment(commentFormDto, userId));
    }

    @Test
    void 없는_댓글에_대댓글_생성() {
        Long userId = 1L;
        Long upperCommentId = 2L;
        CommentFormDto commentFormDto = new CommentFormDto(1L, upperCommentId, "대댓글");

        assertThrows(NoSuchElementException.class,
                () -> commentService.insertComment(commentFormDto, userId));
    }

    @Test
    void 댓글_수정_성공() {
        Long userId = 1L;
        Long commentId = 1L;
        CommentFormDto commentFormDto = new CommentFormDto(null, null, "수정 댓글");

        commentService.updateComment(commentId, commentFormDto, userId);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setUser(userRepository.findById(1L).get());
        comment.setContext(commentFormDto.getContext());
        verify(commentRepository, times(1))
                .save(eq(comment));
    }

    @Test
    void 없는_댓글_수정() {
        Long userId = 1L;
        Long commentId = 2L;
        CommentFormDto commentFormDto = new CommentFormDto(null, null, "수정 댓글");

        assertThrows(NoSuchElementException.class,
                () -> commentService.updateComment(commentId, commentFormDto, userId));
    }

    @Test
    void 타인_소유의_댓글_수정() {
        Long userId = 2L;
        Long commentId = 1L;
        CommentFormDto commentFormDto = new CommentFormDto(null, null, "수정 댓글");

        assertThrows(AccessDeniedException.class,
                () -> commentService.updateComment(commentId, commentFormDto, userId));
    }

    @Test
    void 댓글_삭제_성공() {
        Long userId = 1L;
        Long commentId = 1L;

        commentService.deleteComment(commentId, userId);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setUser(userRepository.findById(1L).get());
        comment.setBlock(true);
        verify(commentRepository, times(1))
                .save(eq(comment));
    }

    @Test
    void 없는_댓글_삭제() {
        Long userId = 1L;
        Long commentId = 2L;

        assertThrows(NoSuchElementException.class,
                () -> commentService.deleteComment(commentId, userId));
    }

    @Test
    void 타인_소유의_댓글_삭제() {
        Long userId = 2L;
        Long commentId = 1L;

        assertThrows(AccessDeniedException.class,
                () -> commentService.deleteComment(commentId, userId));
    }

    @Test
    void 게시글의_댓글_조회_성공() {
        Long userId = 2L;
        Long postId = 1L;
        Pageable pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "time"));

        commentService.findCommentsInPost(postId, userId, pageable);

        verify(commentRepository, times(1))
                .findByPostId(eq(postId), eq(userId), eq(pageable));
    }

    @Test
    void 없는_게시글의_댓글_조회() {
        Long userId = 2L;
        Long postId = 2L;
        Pageable pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "time"));

        assertThrows(NoSuchElementException.class,
                () -> commentService.findCommentsInPost(postId, userId, pageable));
    }
}
