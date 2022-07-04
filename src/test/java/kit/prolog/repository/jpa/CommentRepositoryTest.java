package kit.prolog.repository.jpa;

import kit.prolog.config.AppConfig;
import kit.prolog.config.QuerydslConfig;
import kit.prolog.domain.*;
import kit.prolog.dto.CommentLv1Dto;
import kit.prolog.dto.CommentLv2Dto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QuerydslConfig.class, AppConfig.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:test.sql"})
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void 댓글_등록() {
        Comment comment = Comment.builder()
                .user(userRepository.findById(1L).get())
                .post(postRepository.findById(1L).get())
                .upperComment(null)
                .context("댓글")
                .build();
        Comment savedComment = commentRepository.save(comment);

        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContext()).isEqualTo(comment.getContext());
        assertThat(savedComment.getBlock()).isEqualTo(false);
        assertThat(savedComment.getTime()).isNotNull();
    }

    @Test
    void 게시글_댓글_조회() {
        List<CommentLv2Dto> commentLv2Dtos = new ArrayList();
        commentLv2Dtos.add(
                CommentLv2Dto.builder()
                        .id(5L)
                        .userId(2L)
                        .nickname("홍길동")
                        .userImage("https://2")
                        .isPostWriter(false)
                        .isCommentWriter(true)
                        .context("요건 대댓글입니다3")
                        .time(LocalDateTime.of(2022, 7, 2, 14, 19, 28, 4000))
                        .build());
        commentLv2Dtos.add(
                CommentLv2Dto.builder()
                        .id(4L)
                        .userId(1L)
                        .nickname("판교")
                        .userImage("https://1")
                        .isPostWriter(true)
                        .isCommentWriter(false)
                        .context("요건 대댓글입니다2")
                        .time(LocalDateTime.of(2022, 7, 2, 14, 19, 28, 3000))
                        .build());
        commentLv2Dtos.add(
                CommentLv2Dto.builder()
                        .id(3L)
                        .userId(2L)
                        .nickname("홍길동")
                        .userImage("https://2")
                        .isPostWriter(false)
                        .isCommentWriter(true)
                        .context("요건 대댓글입니다1")
                        .time(LocalDateTime.of(2022, 7, 2, 14, 19, 28, 2000))
                        .build());

        List<CommentLv1Dto> commentLv1Dtos = new ArrayList();
        commentLv1Dtos.add(
                CommentLv1Dto.builder()
                        .id(2L)
                        .userId(1L)
                        .nickname("판교")
                        .userImage("https://1")
                        .isPostWriter(true)
                        .isCommentWriter(false)
                        .context("이것은 댓글입니다2")
                        .time(LocalDateTime.of(2022, 7, 2, 14, 19, 28, 1000))
                        .lowerComments(commentLv2Dtos)
                        .build());
        commentLv1Dtos.add(
                CommentLv1Dto.builder()
                        .id(1L)
                        .userId(1L)
                        .nickname("판교")
                        .userImage("https://1")
                        .isPostWriter(true)
                        .isCommentWriter(false)
                        .context("이것은 댓글입니다1")
                        .time(LocalDateTime.of(2022, 7, 2, 14, 19, 28, 0))
                        .lowerComments(new ArrayList())
                        .build());

        Pageable pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "time"));
        assertThat(commentRepository.findByPostId(1L, 2L, pageable))
                .isEqualTo(commentLv1Dtos);
    }
}