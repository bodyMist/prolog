package kit.prolog.controller;

import kit.prolog.dto.CommentFormDto;
import kit.prolog.dto.CommentLv1Dto;
import kit.prolog.dto.SuccessDto;
import kit.prolog.service.CommentService;
import kit.prolog.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    @PostMapping("/comments/submitComment")
    public ResponseEntity saveComment(@RequestBody CommentFormDto commentFormDto,
                                      @RequestHeader(value = "X-AUTH-TOKEN", required = false) String accessToken) {

        if (!jwtService.validateToken(accessToken))
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        Long userId = Long.parseLong(jwtService.getUserPk(accessToken));

        commentService.insertComment(commentFormDto, userId);
        return new ResponseEntity(new SuccessDto(true), HttpStatus.OK);
    }

    @PatchMapping("/comments/modifyComment/{id}")
    public ResponseEntity editComment(@PathVariable("id") Long commentId,
                                      @RequestBody CommentFormDto commentFormDto,
                                      @RequestHeader(value = "X-AUTH-TOKEN", required = false) String accessToken) {

        if (!jwtService.validateToken(accessToken))
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        Long userId = Long.parseLong(jwtService.getUserPk(accessToken));

        commentService.updateComment(commentId, commentFormDto, userId);
        return new ResponseEntity(new SuccessDto(true), HttpStatus.OK);
    }

    @DeleteMapping("/comments/deleteComment/{id}")
    public ResponseEntity deleteComment(@PathVariable("id") Long commentId,
                                        @RequestHeader(value = "X-AUTH-TOKEN", required = false) String accessToken) {

        if (!jwtService.validateToken(accessToken))
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        Long userId = Long.parseLong(jwtService.getUserPk(accessToken));

        commentService.deleteComment(commentId, userId);
        return new ResponseEntity(new SuccessDto(true), HttpStatus.OK);
    }

    @GetMapping("/boards/{id}/comments")
    public ResponseEntity getComments(@PathVariable("id") Long postId,
                                      @PageableDefault(sort = "time", direction = Sort.Direction.ASC) Pageable pageable,
                                      @RequestHeader(value = "X-AUTH-TOKEN", required = false) String accessToken) {

        Long userId = null;
        if (jwtService.validateToken(accessToken))
            userId = Long.parseLong(jwtService.getUserPk(accessToken));

        List<CommentLv1Dto> comments = commentService.findCommentsInPost(postId, userId, pageable);
        return new ResponseEntity(new SuccessDto(true, comments), HttpStatus.OK);
    }
}
