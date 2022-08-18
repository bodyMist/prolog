package kit.prolog.controller;

import kit.prolog.dto.CommentFormDto;
import kit.prolog.dto.CommentLv1Dto;
import kit.prolog.dto.SuccessDto;
import kit.prolog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/submitComment")
    public SuccessDto saveComment(@RequestBody CommentFormDto commentFormDto) {
        /* TODO: principal */
        Long userId = 1L;

        commentService.insertComment(commentFormDto, userId);
        return new SuccessDto(true);
    }

    @PatchMapping("/comments/modifyComment/{id}")
    public SuccessDto editComment(@PathVariable("id") Long commentId,
                                  @RequestBody CommentFormDto commentFormDto) {
        /* TODO: principal */
        Long userId = 1L;

        commentService.updateComment(commentId, commentFormDto, userId);
        return new SuccessDto(true);
    }

    @DeleteMapping("/comments/deleteComment/{id}")
    public SuccessDto deleteComment(@PathVariable("id") Long commentId) {
        /* TODO: principal */
        Long userId = 1L;

        commentService.deleteComment(commentId, userId);
        return new SuccessDto(true);
    }

    @GetMapping("/boards/{id}/comments")
    public SuccessDto getComments(@PathVariable("id") Long postId,
                                  @PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable) {

        /* TODO: principal */
        Long userId = 1L;

        List<CommentLv1Dto> comments = commentService.findCommentsInPost(postId, userId, pageable);
        return new SuccessDto(true, comments);
    }
}
