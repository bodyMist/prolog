package kit.prolog.service;

import kit.prolog.controller.CategoryController;
import kit.prolog.domain.Category;
import kit.prolog.dto.CategoryDto;
import kit.prolog.dto.CategoryInfoDto;
import kit.prolog.repository.jpa.CategoryRepository;
import kit.prolog.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public void insertCategory(CategoryController.CategoryFormDto categoryFormDto, Long userId) {
        Category category = Category.builder()
                .name(categoryFormDto.getName())
                .user(userRepository.findById(userId).get())
                .upperCategory(categoryRepository.findById(categoryFormDto.getUpperId()).get())
                .build();
        categoryRepository.save(category);
    }

    public void updateCategory(Long categoryId, CategoryController.CategoryFormDto categoryFormDto, Long userId) {
        Category category = categoryRepository.findById(categoryId).get();
        if (category.getUser().getId() != userId)
            throw new AccessDeniedException("");

        category.setName(categoryFormDto.getName());
        category.setUpperCategory(categoryRepository.findById(categoryFormDto.getUpperId()).get());
        categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId).get();
        if (category.getUser().getId() != userId)
            throw new AccessDeniedException("");

        categoryRepository.delete(category);
    }

    public List<CategoryInfoDto> findCategoryInfos(Long userId) {
        return categoryRepository.findInfoByUserId(userId);
    }
}
