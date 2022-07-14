package kit.prolog.service;

import kit.prolog.domain.Comment;
import kit.prolog.dto.CommentFormDto;
import kit.prolog.dto.CommentLv1Dto;
import kit.prolog.repository.jpa.CommentRepository;
import kit.prolog.repository.jpa.PostRepository;
import kit.prolog.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void insertComment(CommentFormDto commentFormDto, Long userId) {
        Long upperCommentId = commentFormDto.getUpperCommentId();
        Comment upperComment = null;
        if (upperCommentId != null) {
            upperComment = commentRepository.findById(upperCommentId).get();
        }

        Comment comment = Comment.builder()
                .user(userRepository.findById(userId).get())
                .post(postRepository.findById(commentFormDto.getPostId()).get())
                .upperComment(upperComment)
                .context(commentFormDto.getContext())
                .build();
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, CommentFormDto commentFormDto, Long userId) {
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getId() != userId)
            throw new AccessDeniedException("");

        comment.setContext(commentFormDto.getContext());
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getId() != userId)
            throw new AccessDeniedException("");

        comment.setBlock(true);
        commentRepository.save(comment);
    }

    public List<CommentLv1Dto> findCommentsInPost(Long postId, Long userId, Pageable pageable) {
        postRepository.findById(postId).get();
        return commentRepository.findByPostId(postId, userId, pageable);
    }
}
