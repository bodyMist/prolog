package kit.prolog.repository;

import kit.prolog.domain.*;
import kit.prolog.dto.LayoutDto;
import kit.prolog.dto.MoldDto;
import kit.prolog.enums.LayoutType;
import kit.prolog.repository.jpa.LayoutRepository;
import kit.prolog.repository.jpa.MoldRepository;
import kit.prolog.repository.jpa.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MoldLayoutRepositoryTest {
    @Autowired MoldRepository moldRepository;
    @Autowired LayoutRepository layoutRepository;
    @Autowired PostRepository postRepository;

    @Test
    void 레이아웃_상속클래스_저장(){
        Layout layout = layoutRepository.findById(1L).get();
        String test = "test";
        LayoutType layoutType = LayoutType.values()[layout.getDtype()];
        Layout input = null;
        switch (layoutType){
            case CONTEXT:
                input = new Context(test);
                break;
//            case IMAGE:
//                input = new Image(test);
//                break;
//            case CODES:
//                input = new Code(test);
//                break;
            case HYPERLINK:
                input = new Hyperlink(test);
                break;
            case MATHEMATICS:
                input = new Mathematics(test);
                break;
            case VIDEOS:
                input = new Video(test);
                break;
            case DOCUMENTS:
                input = new Document(test);
                break;
            default:
                input = new Layout();
                break;
        }
        layoutRepository.save(input);
        layoutRepository.flush();
    }

    @Test
    void 레이아웃틀_조회(){
        Long userId = 1L;
        List<MoldDto> moldList = moldRepository.findByUser_Id(userId);
        System.out.println(moldList);
        // Projection 확인
        assertThat(moldList.get(0).getClass()).isEqualTo(MoldDto.class);
    }

    @Test
    void 레이아웃_리스트_조회(){
        Long moldId = 1L;
        List<LayoutDto> layoutList = layoutRepository.findByMold_Id(moldId);
        System.out.println(layoutList);
        // Projection 확인
        assertThat(layoutList.get(0).getClass()).isEqualTo(LayoutDto.class);
    }

    @Test
    void 레이아웃틀_삭제(){
        Long moldId = 1L;
        Optional<Mold> mold = moldRepository.findById(moldId);
        List<Post> postList = postRepository.findByMold_Id(moldId);
        postList.forEach(post -> {
            post.setMold(null);
            postRepository.saveAndFlush(post);
        });
        moldRepository.delete(mold.get());
    }

    @Test
    void 게시글_레이아웃_조회(){
        Long moldId = 1L;
        layoutRepository.findLayoutDetailByMold_Id(moldId)
                .forEach(System.out::println);
    }

    @Test
    void 레이아웃_연쇄삭제(){
        Long layoutId = 1L;
        layoutRepository.deleteById(layoutId);
        layoutRepository.flush();
    }
}
