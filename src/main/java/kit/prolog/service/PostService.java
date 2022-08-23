package kit.prolog.service;

import kit.prolog.domain.*;
import kit.prolog.dto.*;
import kit.prolog.enums.CodeType;
import kit.prolog.enums.LayoutType;
import kit.prolog.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/*
* 게시글 API 비즈니스 로직
* 게시글 CRUD, 좋아요/취소, 다양한 화면에서의 목록 조회
* */
@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final MoldRepository moldRepository;
    private final LayoutRepository layoutRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final CommentRepository commentRepository;
    private final HitRepository hitRepository;
    private final ContextRepository contextRepository;

    /**
     * 레이아웃 작성 API
     * 게시글에 포함되는 레이아웃의 틀과 하위 레이아웃들을 저장
     * 매개변수 : userId(사용자 pk), List<LayoutDto> (레이아웃 데이터), arg(레이아웃틀 이름/가변 매개)
     * 반환 : List<LayoutDto> pk를 포함한 저장된 결과 반환
     *
     * !!Warning!!
     * 레이아웃 content는 이후 게시글 작성 API에서 이루어짐
     * update문은 spring jpa save로 사용.
     * 컨테이너에 저장된 bean의 주소값과 자바에서의 객체 주소값이 다를 수 있으므로 주의.
     * */
    public List<LayoutDto> saveLayouts(Long userId, List<LayoutDto> layoutData, String moldName){
        Mold savedMold = null;
        if (!moldName.isEmpty()) {
            savedMold = moldRepository.save(new Mold(moldName, new User(userId)));
        }
        List<Layout> savedLayouts = layoutData.stream().map(Layout::new).collect(Collectors.toList());
        if (savedMold != null) {
            Mold finalSavedMold = savedMold;
            savedLayouts.forEach(layout -> layout.setMold(finalSavedMold));
        }
        List<LayoutDto> result = layoutRepository.saveAll(savedLayouts).stream().map(LayoutDto::new).collect(Collectors.toList());
        return result;
    }

    /**
    * 레이아웃 리스트 조회 API
    * 매개변수 : moldId(레이아웃 틀 pk)
    * 반환 : List<LayoutDto>
    * */
    public List<LayoutDto> viewLayoutsByMold(Long moldId){
        return layoutRepository.findLayoutDtoByMold_Id(moldId);
    }

    /**
    * 레이아웃 틀 리스트 조회 API
    * 매개변수 : userId(회원 pk)
    * 반환 : List<MoldDto>
    * */
    public List<MoldDto> viewMyMolds(Long userId){
        return moldRepository.findByUser_Id(userId);
    }

    /**
     * 레이아웃 틀 삭제 API
     * 매개변수 : moldId(레이아웃 틀 pk)
     * 반환 : boolean
     * 에러처리 :
     * */
    public boolean deleteMold(Long moldId){
        Optional<Mold> mold = moldRepository.findById(moldId);
        List<Post> postList = postRepository.findByMold_Id(moldId);
        List<Layout> layoutList = layoutRepository.findByMold_Id(moldId);
        postList.forEach(post -> {
            post.setMold(null);
        });
        layoutList.forEach(layout -> {
            layout.setMold(null);
        });
        postRepository.saveAllAndFlush(postList);
        layoutRepository.saveAllAndFlush(layoutList);

        moldRepository.delete(mold.get());
        return true;
    }

    /**
     * 게시글 작성 API 비즈니스 로직
     * 매개변수 : userId(사용자 pk), moldId(레이아웃 틀 pk),
     *          title(게시글 제목), layouts(레이아웃 데이터 리스트), categoryid(카테고리 pk),
     *          param(태그 또는 첨부파일)
     * 반환 : Long(게시글 pk)
     * 에러처리 :
     * */
    public Long writePost(Long userId, String title,
                          List<LayoutDto> layoutDtos, Long categoryId,
                          HashMap<String, Object> param){
        Optional<Mold> mold;

        Optional<User> user = userRepository.findById(userId);
        Optional<Category> category = categoryRepository.findById(categoryId);

        Post post = new Post(title, LocalDateTime.now(),user.get(), category.get());
        if (param.containsKey("moldId")){
            Long moldId = Long.parseLong(param.get("moldId").toString());
            mold = moldRepository.findById(moldId);
            post.setMold(mold.get());
        }
        Post savedPost = postRepository.save(post);

        layoutDtos.forEach(layoutDto -> {
            layoutRepository.findLayoutById(layoutDto.getId()).ifPresent(layout -> {
                LayoutType layoutType = LayoutType.values()[layout.getDtype()];

                List<Context> contextList = new ArrayList<>();
                Context context = new Context(layoutDto.getLeader(), savedPost, layout);
                switch (layoutType){
                    case CONTEXT:
                    case MATHEMATICS:
                        context.setContext(layoutDto.getContent());
                        break;
                    case IMAGE:
                        layoutDto.getUrl().forEach(url -> {
                            contextList.add(new Context(url, layoutDto.getLeader(), savedPost, layout));
                        });
                        break;
                    case CODES:
                        List<String> codes = layoutDto.getCodes();
                        context.setCode(codes.get(0));
                        context.setCodeExplanation(codes.get(1));
                        context.setCodeType(CodeType.valueOf(codes.get(2)));
                        break;
                    case HYPERLINK:
                    case VIDEOS:
                    case DOCUMENTS:
                        context.setUrl(layoutDto.getContent());
                        break;
                }

                if(contextList.isEmpty()){
                    contextRepository.save(context);
                }else{
                    contextList.forEach(contextRepository::save);
                }
            });
        });
        if (!param.isEmpty()){
            if(param.containsKey("tags")){
                List<String> tagList = (List<String>) param.get("tags");
                tagList.forEach(tag -> {
                    Optional<Tag> optionalTag = tagRepository.findByName(tag);
                    if(!optionalTag.isPresent()) {
                        optionalTag = Optional.of(tagRepository.save(new Tag(tag)));
                    }
                    PostTag postTag = new PostTag(savedPost, optionalTag.get());
                    postTagRepository.save(postTag);
                });
            }
            if(param.containsKey("attachments")){
                List<AttachmentDto> attachmentList = (List<AttachmentDto>) param.get("attachments");
                attachmentList.forEach(attachmentDto -> {
                    Attachment attachment = new Attachment(attachmentDto, savedPost);
                    attachmentRepository.save(attachment);
                });
            }
        }

        return savedPost.getId();
    }

    /**
    * 특정 카테고리 게시글 조회 API
    * 매개변수 : account(사용자 계정), categoryName(카테고리명), cursor(마지막 게시글 pk)
    * 반환 : List<PostPreviewDto>
    * */
    public List<PostPreviewDto> viewPostsByCategory(String account, String categoryName, int cursor){
        return postRepository.findPostByCategoryName(account, categoryName, cursor);
    }

    /**
    * 게시글 상세조회 API
    * 매개변수 : postId(게시글 pk)
    * 반환 : PostDetailDto = 회원정보(작성자 닉네임, 이미지), 게시글정보(pk,제목,작성일자),
    *                       레이아웃틀 pk, 레이아웃 리스트(pk, type, x/y 좌표, 가로/세로, explanation, content),
    *                       카테고리(pk, name), 첨부파일 리스트(pk,이름,url), 태그 리스트(이름),
    *                       조회수, 좋아요(count, exist), 댓글(id, 작성자, 내용, 작성일자, 상위댓글, block 여부)
    * 에러처리 :
    * QueryDSL : 게시글을 기준으로 카테고리, 회원, 레이아웃 틀, 조회수 데이터를 조회
    * Spring JPA : 댓글, 좋아요 , 첨부파일(리스트), 태그(리스트), 레이아웃(리스트)는 별도 쿼리로 조회하여 전달
    * */
    public PostDetailDto viewPostDetailById(Long userId, Long postId){
        PostDetailDto postDetailDto = postRepository.findPostById(postId);
        boolean exist;
        int likeCount = likeRepository.countByPost_Id(postId);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time"));
        List<CommentLv1Dto> commentList = commentRepository.findByPostId(postId, userId, pageable);

        List<AttachmentDto> attachmentList = attachmentRepository.findByPost_Id(postId);
        List<String> tagList = new ArrayList<>();
        List<String> optionalTagName = postTagRepository.findTagNameByPost_Id(postId);
        if(!optionalTagName.isEmpty())
            tagList.addAll(optionalTagName);

        //레이아웃 가져오기
        List<LayoutDto> layoutList = postRepository.selectDetailLayout(postId);
        Map<Long, LayoutDto> layoutId = new HashMap<>();
        layoutList.forEach(layout -> {
            if(layout.getDtype() == LayoutType.IMAGE.getValue()){
                if(layoutId.containsKey(layout.getId())){
                    layoutId.get(layout.getId()).addUrl(layout);
                }else {
                    layoutId.put(layout.getId(), layout);
                }
            }else{
                layoutId.put(layout.getId(), layout);
            }
        });

        LikeDto like = new LikeDto(likeCount);
        if (userId != null) {
            exist = likeRepository.existsByUser_IdAndPost_Id(userId, postId);
            like.setExist(exist);
        }
        postDetailDto.setLikeDto(like);
        postDetailDto.setComments(commentList);
        postDetailDto.setAttachmentDto(attachmentList);
        postDetailDto.setTags(tagList);
        postDetailDto.setLayoutDto(new ArrayList<>(layoutId.values()));

        return postDetailDto;
    }

    /**
     * 게시글 수정 API
     * 매개변수 :
     * 반환 :
     * 발생 가능 에러 :
     * */
    public boolean updatePost(Long postId, Long userId, String title,
                              List<LayoutDto> layoutDtos, Long categoryId,
                              HashMap<String, Object> param){
        Optional<Mold> mold;

        Optional<User> user = userRepository.findById(userId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        Post post = new Post();
        post.setId(postId);
        postRepository.save(post);

        return true;
    }

    /**
    * 게시글 삭제 API
    * 매개변수 : postId(게시글 pk)
    * 반환 : boolean
    * 발생 가능 에러 : IllegalArg, SQL Error(?)
    * */
    public boolean deletePost(Long postId){
        // 좋아요 -> 댓글 -> 조회수 -> 첨부파일 -> postTag -> 내용 -> 게시글
        likeRepository.deleteAllByPost_Id(postId);
        commentRepository.deleteAllByPost_Id(postId);
        hitRepository.deleteAllByPost_Id(postId);
        // 파일서버에 삭제 요청 필요
        attachmentRepository.deleteAllByPost_Id(postId);
        postTagRepository.deleteAllByPost_Id(postId);

        contextRepository.deleteAllByPost_Id(postId);

        postRepository.deleteById(postId);
        return true;
    }

    /**
     * 태그 조회 API
     * 매개변수 : tagName(태그 이름)
     * 반환 : List<String>
     * 발생 가능 에러 :
     * */
    public List<String> findTagByName(String tagName){
        return tagRepository.findByNameStartingWith(tagName)
                .stream().map(Tag::getName).collect(Collectors.toList());
    }

    /**
    * 게시글 좋아요 API
    * 매개변수 : postId(게시글 pk), userId(사용자 pk)
    * 반환 : boolean
    * 발생 가능 에러 : DataIntegrityViolationException(잘못된 데이터가 바인딩 되었을 때 발생)
    * */
    public boolean likePost(Long userId, Long postId){
        Optional<Like> like = likeRepository.findByUser_IdAndPost_Id(userId, postId);
        boolean result = false;
        if(like.isPresent()){
            likeRepository.delete(like.get());
            log.info("좋아요 취소 Service 실행");
        }else{
            Like myLike = new Like(new User(userId), new Post(postId));
            likeRepository.save(myLike);
            log.info("좋아요 등록 Service 실행");
            result = true;
        }
        return result;
    }

    /**
     * 내가 쓴 글 목록 조회 API
     * 매개변수 : userId(사용자 pk), cursor(페이지 번호)
     * 반환 : List<PostPreviewDto>
     * */
    public List<PostPreviewDto> getMyPostList(String account, int cursor){
        return postRepository.findMyPostByUserId(account, cursor);
    }

    /**
     * 좋아요 한 글 목록 조회 API
     * 매개변수 : userId(사용자 pk), cursor(페이지 번호)
     * 반환 : List<PostPreviewDto>
     * */
    public List<PostPreviewDto> getLikePostList(String account, int cursor){
        return postRepository.findLikePostByUserId(account, cursor);
    }

    /**
     * 전체 게시글 목록 조회 API
     * 최근 날짜동안의 좋아요 상승률 내림차순으로 게시글 검색
     * 매개변수 : cursor(페이지 번호)
     * 반환 : List<PostPreviewDto>
     * */
    public List<PostPreviewDto> getHottestPostList(int page, int size){
        return postRepository.findHottestPosts(page);
    }

    /**
     * 최근 게시글 목록 조회 API
     * 매개변수 : cursor(페이지 번호)
     * 반환 : List<PostPreviewDto>
     * */
    public List<PostPreviewDto> getRecentPostList(int cursor){
        return postRepository.findRecentPosts(cursor);
    }
}
