package kit.prolog.repository;


import kit.prolog.config.EmailConfig;
import kit.prolog.config.QuerydslConfig;
import kit.prolog.domain.*;
import kit.prolog.domain.Tag;
import kit.prolog.dto.LayoutDto;
import kit.prolog.dto.MoldDto;
import kit.prolog.dto.PostDetailDto;
import kit.prolog.enums.LayoutType;
import kit.prolog.repository.jpa.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({QuerydslConfig.class, EmailConfig.class})
@ActiveProfiles("test")
@Sql(scripts = {"/test.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private MoldRepository moldRepository;
    @Autowired private LayoutRepository layoutRepository;
    @Autowired private ContextRepository contextRepository;


    @Test
    @DisplayName("틀 없이 게시글 저장")
    public void savePostWithNoMold(){
        // given
        User user = userRepository.findOneById(1L);
        Optional<Category> category = categoryRepository.findById(1L);
        Post post = new Post("test-title", LocalDateTime.now(), user, category.get());
        Layout layout = layoutRepository.getById(1L);
        Context context = new Context(true, post, layout);

        // when
        Post savedPost = postRepository.save(post);
        Layout updatedLayout = layoutRepository.save(layout);
        Context savedContext = contextRepository.save(context);

        // then
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(savedPost.getUser().getId()).isEqualTo(user.getId());

        assertThat(updatedLayout.getCoordinateX()).isEqualTo(layout.getCoordinateX());

        assertThat(savedPost.getId()).isEqualTo(savedContext.getPost().getId());
    }

    @Test
    @DisplayName("틀 사용 게시글 저장")
    public void savePostWithMold(){
        // given
        User user = userRepository.findOneById(1L);
        Optional<Category> category = categoryRepository.findById(1L);
        Mold mold = moldRepository.getById(1L);
        Post post = new Post("test-title", LocalDateTime.now(), user, category.get());
        post.setMold(mold);
        Layout layout = layoutRepository.getById(1L);
        layout.setExplanation("테스트용가리");
        layout.setWidth(100.0);
        layout.setHeight(100.0);
        layout.setCoordinateX(100.0);
        layout.setCoordinateY(100.0);
        Context context = new Context(true, post, layout);

        // when
        Post savedPost = postRepository.save(post);
        Layout updatedLayout = layoutRepository.save(layout);
        Context savedContext = contextRepository.save(context);

        // then
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getMold()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(savedPost.getUser().getId()).isEqualTo(user.getId());

        assertThat(updatedLayout.getExplanation()).isEqualTo("테스트용가리");

        assertThat(savedPost.getId()).isEqualTo(savedContext.getPost().getId());
    }

    @Test
    @DisplayName("레이아웃 작성")
    public void writeLayout(){
        // given
        List<LayoutDto> layoutDtoList = new ArrayList<>();
        LayoutDto one = new LayoutDto();
        one.setDtype(1);
        one.setCoordinateX(10.0);
        one.setCoordinateY(20.0);
        one.setWidth(15.0);
        one.setHeight(25.0);
        layoutDtoList.add(one);
        List<Layout> layouts = layoutDtoList.stream().map(Layout::new).collect(Collectors.toList());

        // when
        List<Layout> savedLayouts = layoutRepository.saveAll(layouts);

        // then
        assertThat(savedLayouts).isNotEmpty();
        assertThat(savedLayouts.size()).isEqualTo(layouts.size());
    }
    @Test
    @DisplayName("틀에 레이아웃 작성")
    public void writeLayoutwithMold(){
        // given
        User user = userRepository.findOneById(1L);
        Mold mold = new Mold("moldName", user);
        List<LayoutDto> layoutDtoList = new ArrayList<>();
        LayoutDto one = new LayoutDto();
        one.setDtype(1);
        one.setCoordinateX(10.0);
        one.setCoordinateY(20.0);
        one.setWidth(15.0);
        one.setHeight(25.0);
        layoutDtoList.add(one);
        List<Layout> layouts = layoutDtoList.stream().map(Layout::new).collect(Collectors.toList());

        // when
        Mold savedMold = moldRepository.save(mold);
        layouts.forEach(layout -> layout.setMold(savedMold));
        List<Layout> savedLayouts = layoutRepository.saveAll(layouts);

        // then
        assertThat(savedLayouts).isNotEmpty();
        assertThat(savedLayouts.size()).isEqualTo(layouts.size());
        assertThat(savedLayouts.stream().allMatch(layout -> layout.getMold().equals(savedMold))).isTrue();
    }

    @Test
    @DisplayName("레이아웃 리스트 조회")
    public void selectLayouts(){
        // given
        Mold mold = moldRepository.findById(1L).get();
        // when
        List<LayoutDto> layoutDto = layoutRepository.findLayoutDtoByMold_Id(mold.getId());
        // then
        assertThat(layoutDto.size()).isNotZero();
    }

    @Test
    @DisplayName("레이아웃 틀 리스트 조회")
    public void selectMolds(){
        // given
        User user = userRepository.findOneById(1L);
        MoldDto first = new MoldDto(1L, "기본 틀");
        MoldDto second = new MoldDto(2L, "복합 틀");
        List<MoldDto> expected = List.of(first, second);
        // when
        List<MoldDto> moldList = moldRepository.findByUser_Id(user.getId());
        // then
        assertThat(moldList.size()).isEqualTo(expected.size());
        assertThat(moldList.stream().anyMatch(moldDto -> moldDto.getName().equals(first.getName()))).isTrue();
    }

    @Test
    @DisplayName("틀 없이 게시글 작성")
    public void writePost(){
        // given
        User user = userRepository.findOneById(1L);
        Category category= categoryRepository.findById(1L).get();
        String title = "제목";
        Post post = new Post(title, LocalDateTime.now(), user, category);
        List<LayoutDto> layoutDtos = new ArrayList<>();
        LayoutDto firstLayout = new LayoutDto(1L, 1, 0.5, 0.5, 90.0, 90.0);
        LayoutDto secondLayout = new LayoutDto(2L, 2, 0.5, 0.5, 90.0, 90.0);
        LayoutDto thirdLayout = new LayoutDto(3L, 3, 0.5, 0.5, 90.0, 90.0);
        LayoutDto forthLayout = new LayoutDto(4L, 4, 0.5, 0.5, 90.0, 90.0);
        firstLayout.setContent("글 내용");
        secondLayout.setUrl(List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/GoldenGateBridge-001.jpg/1200px-GoldenGateBridge-001.jpg"));
        thirdLayout.setCodes(List.of("Code", "code explanation", "type"));
        forthLayout.setContent("https://www.google.com/search?q=mui+accordion+custom&oq=&aqs=chrome.5.69i59i450l8.694643353j0j15&sourceid=chrome&ie=UTF-8");
        List<Tag> tagList = new ArrayList<>(List.of(new Tag("react"), new Tag("Javascript")));
        layoutDtos.addAll(List.of(firstLayout, secondLayout, thirdLayout, forthLayout));
        // when
        Post savedPost = postRepository.save(post);
        layoutDtos.forEach(layoutDto -> {
            layoutRepository.findLayoutById(layoutDto.getId()).ifPresent(layout -> {
                layout.setExplanation(layoutDto.getExplanation());
                layout.setCoordinateX(layoutDto.getCoordinateX());
                layout.setCoordinateY(layoutDto.getCoordinateY());
                layout.setWidth(layoutDto.getWidth());
                layout.setHeight(layoutDto.getHeight());

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
                        context.setCodeType(codes.get(2));
                        break;
                    case HYPERLINK:
                    case VIDEOS:
                    case DOCUMENTS:
                        context.setUrl(layoutDto.getContent());
                        break;
                }
                layoutRepository.save(layout);
                if(contextList.isEmpty()){
                    contextRepository.save(context);
                }else{
                    contextList.forEach(contextRepository::save);
                }
            });
        });
        PostDetailDto detail = postRepository.findPostById(savedPost.getId());


        // then
        assertThat(savedPost.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedPost.getCategory().getName()).isEqualTo(category.getName());
        assertThat(layoutDtos.size()).isEqualTo(detail.getLayoutDto().size());
        assertThat(detail.getTags().size()).isEqualTo(tagList.size());
    }
}
