package kit.prolog.service;

import kit.prolog.domain.Category;
import kit.prolog.domain.User;
import kit.prolog.dto.CategoryDto;
import kit.prolog.dto.CategoryInfoDto;
import kit.prolog.repository.jpa.CategoryRepository;
import kit.prolog.repository.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User(1L)));

        Category category = Category.builder()
                .id(1L)
                .name("백엔드")
                .user(userRepository.findById(1L).get())
                .build();

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));
        when(categoryRepository.findById(2L))
                .thenReturn(Optional.empty());

        List<CategoryInfoDto> categoryInfoDtos = new ArrayList();
        categoryInfoDtos.add(new CategoryInfoDto(1L, "백엔드", 4L));

        when(categoryRepository.findInfoByUserId(1L))
                .thenReturn(categoryInfoDtos);
    }

    @Test
    void 카테고리_생성_성공() {
        Long userId = 1L;
        CategoryDto categoryDto = CategoryDto.builder()
                .name("백엔드")
                .build();

        categoryService.insertCategory(categoryDto, userId);

        Category category = Category.builder()
                .name(categoryDto.getName())
                .user(userRepository.findById(userId).get())
                .build();
        verify(categoryRepository, times(1))
                .save(eq(category));
    }

    @Test
    void 카테고리_수정_성공() {
        Long userId = 1L;
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name("프론트엔드")
                .build();

        categoryService.updateCategory(categoryDto, userId);

        Category category = Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .user(userRepository.findById(userId).get())
                .build();
        verify(categoryRepository, times(1))
                .save(eq(category));
    }

    @Test
    void 없는_카테고리_수정() {
        Long userId = 1L;
        CategoryDto categoryDto = CategoryDto.builder()
                .id(2L)
                .name("프론트엔드")
                .build();

        assertThrows(NoSuchElementException.class,
                () -> categoryService.updateCategory(categoryDto, userId));
    }

    @Test
    void 타인_소유의_카테고리_수정() {
        Long userId = 2L;
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name("프론트엔드")
                .build();

        assertThrows(AccessDeniedException.class,
                () -> categoryService.updateCategory(categoryDto, userId));
    }

    @Test
    void 카테고리_삭제_성공() {
        Long userId = 1L;
        Long categoryId = 1L;

        categoryService.deleteCategory(categoryId, userId);

        Category category = Category.builder()
                .id(categoryId)
                .name("백엔드")
                .user(userRepository.findById(userId).get())
                .build();
        verify(categoryRepository, times(1))
                .delete(eq(category));
    }

    @Test
    void 없는_카테고리_삭제() {
        Long userId = 1L;
        Long categoryId = 2L;

        assertThrows(NoSuchElementException.class,
                () -> categoryService.deleteCategory(categoryId, userId));
    }

    @Test
    void 타인_소유의_카테고리_삭제() {
        Long userId = 2L;
        Long categoryId = 1L;

        assertThrows(AccessDeniedException.class,
                () -> categoryService.deleteCategory(categoryId, userId));
    }

    @Test
    void 카테고리_조회_성공() {
        Long userId = 1L;

        List<CategoryInfoDto> categoryInfoDtos = new ArrayList();
        categoryInfoDtos.add(new CategoryInfoDto(1L, "백엔드", 4L));
        assertThat(categoryService.findCategoryInfos(userId))
                .isEqualTo(categoryInfoDtos);
    }
}