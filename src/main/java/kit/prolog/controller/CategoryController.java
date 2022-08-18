package kit.prolog.controller;

import kit.prolog.dto.CategoryInfoDto;
import kit.prolog.dto.SuccessDto;
import kit.prolog.service.CategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public SuccessDto createCategory(@RequestBody CategoryFormDto categoryFormDto) {
        /* TODO: principal */
        Long userId = 1L;

        categoryService.insertCategory(categoryFormDto, userId);
        return new SuccessDto(true);
    }

    @PatchMapping("/categories/{id}")
    public SuccessDto editCategory(@PathVariable("id") Long categoryId,
                                   @RequestBody CategoryFormDto categoryFormDto) {
        /* TODO: principal */
        Long userId = 1L;

        categoryService.updateCategory(categoryId, categoryFormDto, userId);
        return new SuccessDto(true);
    }

    @DeleteMapping("/categories/{id}")
    public SuccessDto removeCategory(@PathVariable("id") Long categoryId) {
        /* TODO: principal */
        Long userId = 1L;

        categoryService.deleteCategory(categoryId, userId);
        return new SuccessDto(true);
    }

    @GetMapping("/users/{id}/categories")
    public SuccessDto getCategories(@PathVariable("id") Long userId) {
        List<CategoryInfoDto> categoryInfos = categoryService.findCategoryInfos(userId);
        return new SuccessDto(true, categoryInfos);
    }

    @Getter
    @ToString
    public static class CategoryFormDto {
        private String name;
        private Long upperId;
    }
}
