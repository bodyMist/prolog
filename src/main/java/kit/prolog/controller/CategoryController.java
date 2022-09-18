package kit.prolog.controller;

import kit.prolog.dto.CategoryInfoDto;
import kit.prolog.dto.SuccessDto;
import kit.prolog.service.CategoryService;
import kit.prolog.service.JwtService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final JwtService jwtService;

    @PostMapping("/categories")
    public ResponseEntity createCategory(@RequestBody CategoryFormDto categoryFormDto,
                                         @RequestHeader(value = "X-AUTH-TOKEN", required = false) String accessToken) {

        if (!jwtService.validateToken(accessToken))
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        Long userId = Long.parseLong(jwtService.getUserPk(accessToken));

        categoryService.insertCategory(categoryFormDto, userId);
        return new ResponseEntity(new SuccessDto(true), HttpStatus.OK);
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity editCategory(@PathVariable("id") Long categoryId,
                                       @RequestBody CategoryFormDto categoryFormDto,
                                       @RequestHeader(value = "X-AUTH-TOKEN", required = false) String accessToken) {

        if (!jwtService.validateToken(accessToken))
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        Long userId = Long.parseLong(jwtService.getUserPk(accessToken));

        categoryService.updateCategory(categoryId, categoryFormDto, userId);
        return new ResponseEntity(new SuccessDto(true), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity removeCategory(@PathVariable("id") Long categoryId,
                                         @RequestHeader(value = "X-AUTH-TOKEN", required = false) String accessToken) {

        if (!jwtService.validateToken(accessToken))
            return new ResponseEntity<SuccessDto>(new SuccessDto(false, "access token invalid"), HttpStatus.valueOf(403));
        Long userId = Long.parseLong(jwtService.getUserPk(accessToken));

        categoryService.deleteCategory(categoryId, userId);
        return new ResponseEntity(new SuccessDto(true), HttpStatus.OK);
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
