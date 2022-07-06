package kit.prolog.service;

import kit.prolog.domain.*;
import kit.prolog.dto.*;
import kit.prolog.repository.jpa.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
* 게시글 API 비즈니스 로직
* 게시글 CRUD, 좋아요/취소, 다양한 화면에서의 목록 조회
* */
@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final MoldRepository moldRepository;
    private final LayoutRepository layoutRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 레이아웃 작성 API - moldId가 없을 때
     * 게시글에 포함되는 레이아웃의 틀과 하위 레이아웃들을 저장
     * 매개변수 : userId(사용자 pk), moldName(레이아웃틀 이름), List<LayoutDto> (레이아웃 데이터)
     * 반환 : List<LayoutDto> pk를 포함한 저장된 결과 반환
     *
     * !!Warning!!
     * 레이아웃 content는 이후 게시글 작성 API에서 이루어짐
     * update문은 spring jpa save로 사용.
     * 컨테이너에 저장된 bean의 주소값과 자바에서의 객체 주소값이 다를 수 있으므로 주의.
     * */
    public List<LayoutDto> saveLayouts(Long userId, String moldName, List<LayoutDto> layoutData){
        Mold savedMold = moldRepository.save(new Mold(moldName, new User(userId)));
        List<LayoutDto> result = new ArrayList<>();
        layoutData.forEach(layoutDto -> {
            Layout layout = layoutRepository.save(new Layout(layoutDto, savedMold));
            result.add(new LayoutDto(layout));
        });
        return result;
    }
    /**
     * 레이아웃 작성 API - moldId가 있을 때
     * 게시글에 포함되는 레이아웃의 틀과 하위 레이아웃들을 저장
     * 매개변수 : userId(사용자 pk), moldId(레이아웃 틀 pk), moldName(레이아웃틀 이름), List<LayoutDto> (레이아웃 데이터)
     * 반환 : List<LayoutDto> pk를 포함한 저장된 결과 반환
     *
     * !!Warning!!
     * 레이아웃 content는 이후 게시글 작성 API에서 이루어짐
     * update문은 spring jpa save로 사용.
     * 컨테이너에 저장된 bean의 주소값과 자바에서의 객체 주소값이 다를 수 있으므로 주의.
     * */
    public List<LayoutDto> saveLayouts(Long userId, Long moldId, String moldName, List<LayoutDto> layoutData){
        List<LayoutDto> result = new ArrayList<>();
        layoutData.forEach(layoutDto -> {
            Layout layout = layoutRepository.save(new Layout(layoutDto, new Mold(moldId)));
            result.add(new LayoutDto(layout));
        });
        return result;
    }

    /**
    * 레이아웃 리스트 조회 API
    * 매개변수 : moldId(레이아웃 틀 pk)
    * 반환 : List<LayoutDto>
    * */
    public List<LayoutDto> viewLayoutsByMold(Long moldId){
        return layoutRepository.findByMold_Id(moldId);
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
        postList.forEach(post -> {
            post.setMold(null);
            postRepository.saveAndFlush(post);
        });
        moldRepository.delete(mold.get());
        return true;
    }

    /**
     * 게시글 작성 API 비즈니스 로직
     * 매개변수 : userId(사용자 pk), moldId(레이아웃 틀 pk),
     *          title(게시글 제목), layouts(레이아웃 데이터 리스트), categoryid(카테고리 pk)
     * 반환 : boolean
     * 에러처리 :
     * */
    public boolean writePost(Long userId, Long moldId, String title,
                             List<LayoutDto> layoutDtos, Long categoryId){
        Optional<Mold> mold = moldRepository.findById(moldId);
        Optional<User> user = userRepository.findById(userId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(!mold.isPresent() || !user.isPresent() || !category.isPresent()){
            return false;
        }

        Post post = new Post(title, LocalDateTime.now(),user.get(), category.get(), mold.get());
        postRepository.save(post);

        layoutDtos.forEach(layoutDto -> {
            layoutRepository.findLayoutById(layoutDto.getId()).ifPresent(layout -> {
                Layout input = null;
                switch (layout.getDtype()){
                    case 1:
                        input = new Context(layoutDto.getContext());
                        break;
                    case 2:
                        input = new Image(layoutDto.getContext());
                        break;
                    case 3:
                        input = new Code(layoutDto.getContext());
                        break;
                    case 4:
                        input = new Hyperlink(layoutDto.getContext());
                        break;
                    case 5:
                        input = new Mathematics(layoutDto.getContext());
                        break;
                    case 6:
                        input = new Video(layoutDto.getContext());
                        break;
                    case 7:
                        input = new Document(layoutDto.getContext());
                        break;
                    default:
                        input = new Layout();
                        break;
                }
                layoutRepository.save(input);
            });
        });
        return true;
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
    * */
    public PostDetailDto viewPostDetailById(Long postId){
        return null;
    }

    /**
    * 게시글 삭제 API
    * 매개변수 : postId(게시글 pk)
    * 반환 : boolean
    * 발생 가능 에러 : IllegalArg, SQL Error(?)
    * */
    public boolean deletePost(Long postId){
        postRepository.deleteById(postId);
        return true;
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
            likeRepository.save(new Like(new User(userId), new Post(postId)));
            log.info("좋아요 등록 Service 실행");
            result = true;
        }
        return result;
    }
}
