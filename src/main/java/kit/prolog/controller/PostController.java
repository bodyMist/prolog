package kit.prolog.controller;

import kit.prolog.dto.*;
import kit.prolog.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * 레이아웃 작성 API
     * userId의 경우 세션에서 가져와야 함.
     * 세션 구현 전까지 직접 클라이언트가 매개로 전송
     * */
    @PostMapping("/layout")
    public SuccessDto createLayout(@RequestHeader(value = "memberpk")Long userId, String moldName, List<LayoutDto> layouts){
        // 세션에서 user 정보 가져와야 함

        List<LayoutDto> layoutDtos = postService.saveLayouts(userId, moldName, layouts);
        return new SuccessDto(true, layoutDtos);
    }
    /**
     * 레이아웃 리스트 조회 API
     * 반환 타입 방법 강구 필요
     * */
    @GetMapping("/layouts/{id}")
    public SuccessDto readLayouts(@PathVariable Long id){
        List<LayoutDto> layoutDtos = postService.viewLayoutsByMold(id);
        return new SuccessDto(true, layoutDtos);
    }

    /**
     * 레이아웃 틀 목록 조회 API
     * */
    @GetMapping("/layouts")
    public SuccessDto readLayoutMolds(@RequestHeader Long userId){
        List<MoldDto> myMolds = postService.viewMyMolds(userId);
        return new SuccessDto(true, myMolds);
    }

    /**
     * 레이아웃 삭제 API
     * */
    @DeleteMapping("/layouts/{id}")
    public boolean deleteMold(@PathVariable Long id){
        return postService.deleteMold(id);
    }

    /**
     * 게시글 작성 API
     * */
    @PostMapping("/board")
    public SuccessDto createPost(@RequestHeader(value = "memberpk")Long userId,
                                 @RequestBody Long moldId,
                                 @RequestBody String title,
                                 @RequestBody List<LayoutDto> layoutDtos,
                                 @RequestBody Long categoryId,
                                 @RequestBody(required = false) List<AttachmentDto> attachments,
                                 @RequestBody(required = false) List<String> tags){
        HashMap<String, Object> params = new HashMap<>();
        if (attachments != null) {
            params.put("attachments", attachments);
        }
        if (tags != null) {
            params.put("tags", tags);
        }
        postService.writePost(userId, moldId, title, layoutDtos, categoryId, params);

        return new SuccessDto(true);
    }

    /**
     * 특정 카테고리 게시글 조회 API
     * */
    @GetMapping("/{user}/{category}")
    public SuccessDto readPostsInCategory(@PathVariable String user,
                                          @PathVariable String category,
                                          @RequestParam int last){
        List<PostPreviewDto> posts = postService.viewPostsByCategory(user, category, last);
        return new SuccessDto(true, posts);
    }
}
