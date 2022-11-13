package kit.prolog.controller;

import kit.prolog.domain.User;
import kit.prolog.dto.*;
import kit.prolog.service.JwtService;
import kit.prolog.service.PostService;
import kit.prolog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private static final Long NO_USER = 0L;
    private static final String SERVER_ERROR = "Unexpected Server Error";
    private final PostService postService;
    private final UserService userService;
    private final JwtService jwtService;
    private final WebClient api;

    /**
     * 레이아웃 작성 API
     * */
    @PostMapping("/layout")
    public SuccessDto createLayout(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                   @RequestBody Map<String, Object> json){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            List<LayoutDto> layouts =
                    ((List<LinkedHashMap>) json.get("layouts"))
                            .stream().map(LayoutDto::new)
                            .collect(Collectors.toList());
            String moldName = json.get("moldName") == null ? "" : json.get("moldName").toString();
            MoldWithLayoutsDto moldWithLayoutsDto = postService.saveLayouts(memberPk, layouts, moldName);
            response = new SuccessDto(true, moldWithLayoutsDto);
        }catch (IllegalArgumentException | NullPointerException exception){
            response = new SuccessDto(false, exception.getMessage());
        }
        return response;
    }
    /**
     * 레이아웃 리스트 조회 API
     * */
    @GetMapping("/layouts/{id}")
    public SuccessDto readLayouts(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                  @PathVariable Long id){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            MoldWithLayoutsDto layoutDtos = postService.viewLayoutsByMold(memberPk, id);
            response = new SuccessDto(true, layoutDtos);
        }catch (IllegalArgumentException | IllegalAccessException | NullPointerException exception){
            response = new SuccessDto(false, exception.getMessage());
        }
        return response;
    }

    /**
     * 레이아웃 틀 목록 조회 API
     * */
    @GetMapping("/layouts")
    public SuccessDto readLayoutMolds(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            List<MoldDto> myMolds = postService.viewMyMolds(memberPk);
            response = new SuccessDto(true, myMolds);
        }catch (IllegalArgumentException | NullPointerException exception){
            response = new SuccessDto(false, exception.getMessage());
        }
        return response;
    }

    /**
     * 레이아웃 삭제 API
     * */
    @DeleteMapping("/layouts/{id}")
    public SuccessDto deleteMold(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                 @PathVariable Long id){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            postService.deleteMold(id, memberPk);
            response = new SuccessDto(true);
        }catch (IllegalArgumentException | NullPointerException exception) {
            response = new SuccessDto(false, exception.getMessage());
        }
        return response;
    }

    /**
     * 게시글 작성 API
     * */
    @PostMapping("/board")
    public SuccessDto createPost(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                 @RequestBody Map<String, Object> json){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            // required
            Long categoryId = Long.parseLong(json.get("category").toString());
            String title = json.get("title").toString();
            List<LayoutDto> layoutDtos = ((List<LinkedHashMap>) json.get("layouts"))
                    .stream().map(LayoutDto::new).collect(Collectors.toList());

            // optional
            Long moldId = json.get("layoutID") == null
                    ? null : Long.parseLong(json.get("layoutID").toString());
            List<String> tags = new ArrayList<>();
            if (json.get("tag") != null) {
                ((List<String>) json.get("tag")).forEach(tags::add);
            }
            List<AttachmentDto> attachments = json.get("attachment") == null
                    ? null : ((List<LinkedHashMap>) json.get("attachment"))
                    .stream().map(AttachmentDto::new)
                    .collect(Collectors.toList());

            HashMap<String, Object> params = new HashMap<>();
            if (moldId != null)
                params.put("moldId", moldId);
            if (attachments != null)
                params.put("attachment", attachments);
            if (!tags.isEmpty())
                params.put("tags", tags);

            Long writePost = postService.writePost(memberPk, title, layoutDtos, categoryId, params);
            response = new SuccessDto(true, writePost);
        }catch (IllegalArgumentException | NullPointerException exception) {
            response = new SuccessDto(false, exception.getMessage());
        }catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }

    /**
     * 특정 카테고리 게시글 조회 API
     * */
    @GetMapping("/{user}/{category}")
    public SuccessDto readPostsInCategory(@PathVariable String user,
                                          @PathVariable String category,
                                          @RequestParam int last){
        List<PostPreviewDto> posts = postService.viewPostsByCategory(user, category, last);
        List<PostPreview> previewList = changeResponseType(posts);
        return new SuccessDto(true, previewList);
    }

    /**
     * 게시글 상세 조회 API
     * 로그인 상태일 때, 좋아요 exist 정보를 포함하여 조회
     * */
    @GetMapping("/board/{id}")
    public SuccessDto readPost(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String accessToken,
                               @PathVariable Long id){
        SuccessDto response;
        PostDetailDto post;
        Long memberPk = null;
        try {
            if(accessToken != null && accessToken.isEmpty())  memberPk = validateUser(accessToken);
            post = postService.viewPostDetailById(Objects.requireNonNullElse(memberPk, NO_USER), id);
            PostDetail postDetail = new PostDetail(post);
            response = new SuccessDto(true, postDetail);
        }catch (NullPointerException | IllegalArgumentException exception){
            response = new SuccessDto(false, exception.getMessage());
        }catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }

    /**
     * 게시글 수정 API
     * */
    @PutMapping("/board/{id}")
    public SuccessDto updatePost(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                 @PathVariable Long id,
                                 @RequestBody Map<String, Object> json){
        SuccessDto response;
        try {
            // required
            Long memberPk = validateUser(accessToken);
            Long categoryId = Long.parseLong(json.get("category").toString());
            String title = json.get("title").toString();
            List<LayoutDto> layoutDtos = ((List<LinkedHashMap>) json.get("layouts"))
                    .stream().map(LayoutDto::new).collect(Collectors.toList());

            // optional
            Long moldId = json.get("moldId") == null
                    ? null : Long.parseLong(json.get("moldId").toString());
            List<String> tags = new ArrayList<>();
            if (json.get("tag") != null) {
                ((List<String>) json.get("tag")).forEach(tags::add);
            }
            List<AttachmentDto> attachments = json.get("attachments") == null
                    ? null : ((List<LinkedHashMap>) json.get("attachments"))
                    .stream().map(AttachmentDto::new)
                    .collect(Collectors.toList());

            HashMap<String, Object> params = new HashMap<>();
            if (moldId != null)
                params.put("moldId", moldId);
            if (attachments != null)
                params.put("attachments", attachments);
            if (!tags.isEmpty())
                params.put("tags", tags);


            Long postId = postService.updatePost(id, memberPk, title, layoutDtos, categoryId, params);
            response = new SuccessDto(true, postId);
        }catch (NullPointerException | IllegalArgumentException exception){
            response = new SuccessDto(false, exception.getMessage());
        }catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }


    /**
     * 게시글 삭제 API
     * */
    @DeleteMapping("/board/{id}")
    public SuccessDto deletePost(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                 @PathVariable Long id){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            postService.deletePost(id, memberPk);
            response = new SuccessDto(true);
        }catch (NullPointerException | IllegalArgumentException e){
            response = new SuccessDto(false, e.getMessage());
        } catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }

    /**
     * 태그 조회 API
     * */
    @GetMapping("/tags")
    public SuccessDto getTags(@RequestParam String name){
        List<String> tags = postService.findTagByName(name);
        return new SuccessDto(true, tags);
    }

    /**
     * 파일 업로드 API
     * */
    @PostMapping("/upload")
    public SuccessDto uploadFiles(@RequestPart(value = "file") List<MultipartFile> files){
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        SuccessDto response;
        try {
            files.forEach(f -> {
                bodyBuilder.part("files", f.getResource());
            });
            List<FileDto> uploadedFiles = api
                    .post()
                    .uri(uriBuilder -> uriBuilder.path("/upload").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<FileDto>>() {
                    })
                    .block();
            List<AttachmentDto> result = postService.saveUploadedFiles(uploadedFiles);
            return new SuccessDto(true, result);
        }catch (IllegalArgumentException exception){
            response = new SuccessDto(false, "No Any File");
        }catch (Exception exception){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }

    /**
     * 파일 삭제 API
     * */
    @DeleteMapping("/upload/{id}")
    public SuccessDto deleteFile(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                 @PathVariable String id){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            Boolean externalResult = api.mutate()
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .delete()
                    .uri(uriBuilder -> uriBuilder.path("/{fileName}").build(id))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            response = externalResult
                    ? new SuccessDto(true, postService.deleteFile(id))
                    : new SuccessDto(false);
        }catch (NullPointerException | IllegalArgumentException exception){
            response = new SuccessDto(false, exception.getMessage());
        }catch (Exception exception){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }

    /**
     * 게시글 좋아요/취소 API
     * */
    @PostMapping("/board/{id}")
    public SuccessDto likePost(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                               @PathVariable Long id){
        Long memberPk = validateUser(accessToken);
        SuccessDto response;
        try{
            boolean like = postService.likePost(memberPk, id);
            response = new SuccessDto(like);
        }catch (IllegalArgumentException argumentException){
            response = new SuccessDto(false, "No User Data");
        }catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }

    /**
     * (구)내가 쓴 글 목록 조회 API
     * */
/*    @GetMapping("/{account}/")
    public SuccessDto readMyPosts(@PathVariable String account, @RequestParam int last){
        List<PostPreviewDto> myPosts = postService.getMyPostList(account, last);
        List<PostPreview> post = changeResponseType(myPosts);
        return new SuccessDto(true, post);
    }*/

    /**
     * (구)좋아요 한 글 목록 조회 API
     * */
/*    @GetMapping("{account}/likes")
    public SuccessDto readLikedPosts(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                     @PathVariable String account, @RequestParam int last){
        Long memberPk = validateUser(accessToken);
        SuccessDto response;
        try {
            List<PostPreviewDto> likedPosts = postService.getLikePostList(memberPk, account, last);
            List<PostPreview> post = changeResponseType(likedPosts);
            response = new SuccessDto(true, post);
        }catch (IllegalArgumentException argumentException){
            response = new SuccessDto(false, "No User Data");
        }catch (NullPointerException nullException){
            response = new SuccessDto(false, nullException.getMessage());
        }
        return response;
    }*/

    /**
     * 내가 쓴 글 목록 조회 API
     * */
    @GetMapping("/my-info/boards")
    public SuccessDto readMyPosts(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                  @RequestParam int last){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            List<PostPreviewDto> myPosts = postService.getMyPostList(memberPk, last);
            List<PostPreview> post = changeResponseType(myPosts);
            response = new SuccessDto(true, post);
        }catch (IllegalArgumentException exception){
            response = new SuccessDto(false, exception.getMessage());
        }catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }

    /**
     * 좋아요 한 글 목록 조회 API
     * */
    @GetMapping("/my-info/likes")
    public SuccessDto readLikedPosts(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                     @RequestParam int last){
        SuccessDto response;
        try {
            Long memberPk = validateUser(accessToken);
            List<PostPreviewDto> likedPosts = postService.getLikePostList(memberPk, last);
            List<PostPreview> post = changeResponseType(likedPosts);
            response = new SuccessDto(true, post);
        }catch (NullPointerException | IllegalArgumentException exception) {
            response = new SuccessDto(false, exception.getMessage());
        }catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }

    /**
     * 전체 게시글 목록 조회 API
     * 최근 좋아요 많이 받은 게시글 리스트 조회
     * 메인화면
     * */
    @GetMapping("/")
    public SuccessDto readHottestPosts(@RequestParam int last){
        List<PostPreviewDto> hottestPosts = postService.getHottestPostList(last);
        List<PostPreview> post = changeResponseType(hottestPosts);
        return new SuccessDto(true, post);
    }

    /**
     * 최근 게시글 목록 조회 API
     * */
    @GetMapping("/recent/")
    public SuccessDto readRecentPosts(@RequestParam int last){
        List<PostPreviewDto> recentPostList = postService.getRecentPostList(last);
        List<PostPreview> post = changeResponseType(recentPostList);
        return new SuccessDto(true, post);
    }

    /**
     * 검색 기능 API
     * */
    @GetMapping("/search")
    public SuccessDto searchPosts(@RequestParam String keyword, @RequestParam int last){
        List<PostPreviewDto> searchPosts = postService.searchPosts(keyword, last);
        List<PostPreview> post = changeResponseType(searchPosts);
        return new SuccessDto(true, post);
    }

    private List<PostPreview> changeResponseType(List<PostPreviewDto> serviceOutput){
        return serviceOutput.stream().map(PostPreview::new).collect(Collectors.toList());
    }

    private Long validateUser(String accessToken) throws NullPointerException, IllegalArgumentException{
        String memberPk = jwtService.validateToken(accessToken) ? jwtService.getUserPk(accessToken) : null;
        if (memberPk == null) throw new NullPointerException("No User Data");
        User user = userService.readUser(Long.valueOf(memberPk));
        if (Long.parseLong(memberPk) != user.getId()) throw new IllegalArgumentException("No Permissions");
        return Long.parseLong(memberPk);
    }

    /**
     * Inner Class For Response
     * */
    @Data
    @AllArgsConstructor
    class Image{
        String url;
    }
    @Data
    class MainLayout{
        private int type;
        private Double width;
        private Double height;
        private String content;
        private List<Image> images;
        private List<String> codes;
        private String explanation;

        MainLayout(LayoutDto dto){
            this.type = dto.getDtype();
            this.width = dto.getWidth();
            this.height = dto.getHeight();
            this.content = dto.getContent();
            this.images = dto.getUrl().stream().map(Image::new).collect(Collectors.toList());
            this.codes = dto.getCodes();
            this.explanation = dto.getExplanation();
        }
    }
    @Data
    class PostPreview{
        private Long id;
        private String title;
        private LocalDate written;
        private String member;
        private String memberImage;
        private Integer likes;
        private Integer hits;
        private MainLayout mainLayout;

        PostPreview(PostPreviewDto dto){
            this.id = dto.getPostDto().getId();
            this.title = dto.getPostDto().getTitle();
            this.written = dto.getPostDto().getTime();
            this.member = dto.getUserDto().getName();
            this.memberImage = dto.getUserDto().getImage();
            this.likes = dto.getLikes().intValue();
            this.hits = dto.getHits().intValue();
            this.mainLayout = new MainLayout(dto.getLayoutDto());
        }
    }

    @Data
    class PostDetail{
        private UserDto user;
        private PostDto post;
        private Long layoutId;  // moldId
        private List<LayoutDetail> layouts;
        private CategoryDto category;
        private List<AttachmentDto> attachment;
        private List<String> tag;
        private Long hits;
        private LikeDto likes;
        private List<CommentDto> comments;

        PostDetail(PostDetailDto dto){
            this.user = dto.getUserDto();
            this.post = dto.getPostDto();
            this.layoutId = dto.getMoldId();
            this.layouts = dto.getLayoutDto().stream().map(LayoutDetail::new).collect(Collectors.toList());
            this.category = dto.getCategoryDto();
            this.attachment = dto.getAttachmentDto();
            this.tag = dto.getTags();
            this.hits = dto.getHits();
            this.likes = dto.getLikeDto();
        }
    }
    @Getter
    class LayoutDetail extends MainLayout{
        private Long id;
        private double coordinateX;
        private double coordinateY;
        private boolean leader;

        LayoutDetail(LayoutDto dto) {
            super(dto);
            this.id = dto.getId();
            this.coordinateX = dto.getCoordinateX();
            this.coordinateY = dto.getCoordinateY();
            this.leader = dto.getLeader();
        }
    }
}