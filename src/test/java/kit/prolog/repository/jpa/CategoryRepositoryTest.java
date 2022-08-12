package kit.prolog.repository.jpa;

import kit.prolog.config.QuerydslConfig;
import kit.prolog.domain.Category;
import kit.prolog.dto.CategoryInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 카테고리_등록() {
        Category category = Category.builder()
                .name("백엔드")
                .user(userRepository.findById(1L).get())
                .upperCategory(categoryRepository.findById(1L).get())
                .build();
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    void 카테고리_정보_조회() {
        List<CategoryInfoDto> expected = List.of(
                new CategoryInfoDto(1L, "전체", 2L, List.of(
                        new CategoryInfoDto(2L, "개발용", 2L,
                                List.of(new CategoryInfoDto(4L, "전시용", 0L, null))),
                        new CategoryInfoDto(3L, "취미용", 0L, List.of()))));

        assertThat(categoryRepository.findInfoByUserId(1L))
                .isEqualTo(expected);
    }
}