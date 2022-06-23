package kit.prolog.service;

import kit.prolog.dto.LayoutDto;
import kit.prolog.dto.PostPreviewDto;
import kit.prolog.dto.SuccessDto;
import kit.prolog.repository.jpa.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    // 게시글 작성 API 비즈니스 로직
    // Request Body 데이터를 수정할 필요가 보임
    public boolean writePost(Long userId, Long moldId, String title,
                                List<LayoutDto> layouts, Long categoryId, Map<String, List<Object>>... args){

        return true;
    }

    /*
    * 특정 카테고리 게시글 조회 API
    * 매개변수 : userName(사용자 계정), categoryName(카테고리명), cursor(마지막 게시글 pk)
    * 반환 :
    * */
    public List<PostPreviewDto> viewPostsByCategory(String userName, String categoryName, int cursor){
        return null;
    }
}
