package kit.prolog.service;

import kit.prolog.domain.Like;
import kit.prolog.domain.Post;
import kit.prolog.domain.User;
import kit.prolog.dto.*;
import kit.prolog.repository.jpa.LayoutRepository;
import kit.prolog.repository.jpa.LikeRepository;
import kit.prolog.repository.jpa.MoldRepository;
import kit.prolog.repository.jpa.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     *게시글 작성 API 비즈니스 로직
     * Request Body 데이터를 수정할 필요가 보임
     * */
    public boolean writePost(Long userId, Long moldId, String title,
                                List<LayoutDto> layouts, Long categoryId, Map<String, List<Object>>... args){

        return true;
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
