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
     * */
    @PostMapping("/layout")
    public SuccessDto createLayout(Long userId, String moldName, List<LayoutDto> layouts){
        // 세션에서 user 정보 가져와야 함

        List<LayoutDto> layoutDtos = postService.saveLayouts(userId, moldName, layouts);
        return new SuccessDto(true, layoutDtos);
    }
    /**
     * 레이아웃 리스트 조회 API
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
    public SuccessDto readLayoutMolds(@RequestBody Long userId){
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
    public SuccessDto createPost(@RequestBody Long userId,
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

    /**
     * 게시글 상세 조회 API
     * 로그인 상태와 비로그인 상태에서 차이 있음  --> 세션 구현 필요
     * 로그인 상태일 때, 좋아요 exist 정보를 포함하여 조회
     * */
    @GetMapping("/board/{id}")
    public SuccessDto readPost(@PathVariable Long id){
        PostDetailDto post;
        // 로그인 상태
        post = postService.viewPostDetailById(null, id);
        // 비로그인 상태
        post = postService.viewPostDetailById(null, id);
        return new SuccessDto(true, post);
    }

    /**
     * 게시글 수정 API
     * */


    /**
     * 게시글 삭제 API
     * deletePost 에서 post 작성자와 Request 주체의 id 비교 필요(Authentication)
     * */
    @DeleteMapping("/board/{id}")
    public SuccessDto deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return new SuccessDto(true);
    }

    /**
     * 태그 조회 API
     * */
    @GetMapping("/tags")
    public SuccessDto getTags(@RequestParam String keyword){
        List<String> tags = postService.findTagByName(keyword);
        return new SuccessDto(true, tags);
    }

    /**
     * 파일 업로드 API
     * */


    /**
     * 파일 삭제 API
     * */

    /**
     * 게시글 좋아요/취소 API
     * */
    @PostMapping("/board/{id}")
    public SuccessDto likePost(@PathVariable Long id, @RequestBody Long userId){
        boolean like = postService.likePost(id, userId);
        return new SuccessDto(like);
    }

    /**
     * 내가 쓴 글 목록 조회 API
     * */
    @GetMapping("{userId}/")
    public SuccessDto readMyPosts(@PathVariable Long userId, @RequestParam int last){
        List<PostPreviewDto> myPosts = postService.getMyPostList(userId, last);
        return new SuccessDto(true, myPosts);
    }

    /**
     * 좋아요 한 글 목록 조회 API
     * */
    @GetMapping("{userId}/likes")
    public SuccessDto readLikedPosts(@PathVariable Long userId, @RequestParam int last){
        List<PostPreviewDto> likedPosts = postService.getLikePostList(userId, last);
        return new SuccessDto(true, likedPosts);
    }

    /**
     * 전체 게시글 목록 조회 API
     * */

    /**
     * 최근 게시글 목록 조회 API
     * */
}
