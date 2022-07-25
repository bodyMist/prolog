package kit.prolog.repository;

import kit.prolog.domain.*;
import kit.prolog.dto.LayoutDto;
import kit.prolog.dto.MoldDto;
import kit.prolog.enums.LayoutType;
import kit.prolog.repository.jpa.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MoldLayoutRepositoryTest {
    @Autowired MoldRepository moldRepository;
    @Autowired LayoutRepository layoutRepository;
    @Autowired PostRepository postRepository;

    @Autowired ImageRepository imageRepository;
    @Autowired CodeRepository codeRepository;

    @Test
    void 이미지_레이아웃_저장(){
        Layout layout = layoutRepository.findById(2L).get();
        String[] rawImages = {
                "https://i.natgeofe.com/n/f7732389-a045-402c-bf39-cb4eda39e786/scotland_travel_4x3.jpg",
                "https://www.tusktravel.com/blog/wp-content/uploads/2020/07/Best-Time-to-Visit-Darjeeling-for-Honeymoon.jpg"
        };
        List<String> images = Arrays.asList(rawImages);
        List<Image> imageList = images.stream()
                .map(Image::new).collect(Collectors.toList());
        imageList.forEach(image -> {
            image.setLayout(layout, Integer.toUnsignedLong(imageList.indexOf(image)));
            System.out.println(image);
            imageRepository.saveImage(image.getLayout().getId(), image.getSequence(), image.getUrl());
        });
    }

    @Test
    void 코드_레이아웃_저장(){
        Layout layout = layoutRepository.findById(2L).get();
        String[][] rawCodes = {
                {
                    "code1", "explanation1"
                },{
                "code2", "explanation2"
        }};
        List<List<String>> codes = Arrays.stream(rawCodes).map(Arrays::asList).collect(Collectors.toList());

        List<Code> codeList = codes
                .stream().map(Code::new).collect(Collectors.toList());
        codeList.forEach(code -> {
            code.setLayout(layout);
            code.setSequence(Integer.toUnsignedLong(codeList.indexOf(code)));
            codeRepository.saveCode(code.getLayout().getId(), code.getSequence(), code.getCode(), code.getCodeExplanation());
        });
    }

    @Test
    void 레이아웃_상속클래스_저장(){
        Layout layout = layoutRepository.findById(2L).get();
        String test = "test";
        String[][] rawCode = {{"코드코드1","설명설명1"},{"코드코드2","설명설명2"},{"코드코드3","설명설명3"}};
        String[] rawImages = {
                "https://i.natgeofe.com/n/f7732389-a045-402c-bf39-cb4eda39e786/scotland_travel_4x3.jpg",
                "https://www.tusktravel.com/blog/wp-content/uploads/2020/07/Best-Time-to-Visit-Darjeeling-for-Honeymoon.jpg"
        };
        LayoutType layoutType = LayoutType.values()[layout.getDtype()];
        Layout input = null;

        switch (layoutType){
            case CONTEXT:
                input = new Context(test);
                break;
            case IMAGE:
                List<String> images = Arrays.asList(rawImages);
                List<Image> imageList = images.stream()
                        .map(Image::new).collect(Collectors.toList());
                imageList.forEach(image -> {
                    image.setLayout(layout, Integer.toUnsignedLong(imageList.indexOf(image)));
                    imageRepository.saveImage(image.getLayout().getId(), image.getSequence(), image.getUrl());
                });
                input = new Layout();
                break;
            case CODES:
                List<List<String>> codes = Arrays.stream(rawCode).map(Arrays::asList).collect(Collectors.toList());
                List<Code> codeList = codes.stream()
                        .map(Code::new).collect(Collectors.toList());
                codeList.forEach(code -> {
                    code.setLayout(layout);
                    code.setSequence(Integer.toUnsignedLong(codeList.indexOf(code)));
                    codeRepository.saveCode(code.getLayout().getId(), code.getSequence(), code.getCode(), code.getCodeExplanation());
                });
                input = new Layout();
                break;
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
        Optional<Mold> mold = moldRepository.findById(1L);
        input.setMold(mold.get());
        input.setExplanation(layout.getExplanation());
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
