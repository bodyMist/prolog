package kit.prolog.repository.jpa;

import kit.prolog.config.QuerydslConfig;
import kit.prolog.dto.CategoryInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:test.sql"})
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void 카테고리_정보_조회() {
        List<CategoryInfoDto> expected = new ArrayList();
        expected.add(new CategoryInfoDto(1L, "개발용", 2L));
        expected.add(new CategoryInfoDto(2L, "취미용", 3L));
        expected.add(new CategoryInfoDto(3L, "전시용", 1L));

        assertThat(categoryRepository.findInfoByUserId(1L))
                .isEqualTo(expected);
    }
}