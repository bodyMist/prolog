package kit.prolog.service;

import kit.prolog.controller.CategoryController;
import kit.prolog.domain.Category;
import kit.prolog.dto.CategoryInfoDto;
import kit.prolog.repository.jpa.CategoryRepository;
import kit.prolog.repository.jpa.PostRepository;
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
    private final PostRepository postRepository;

    public void insertCategory(CategoryController.CategoryFormDto categoryFormDto, Long userId) {
        Category upperCategory = categoryRepository.findById(categoryFormDto.getUpperId()).get();
        if (upperCategory.getUser().getId() != userId)
            throw new AccessDeniedException("");

        if (upperCategory.getUpperCategory() != null
                && upperCategory.getUpperCategory().getUpperCategory() != null)
            throw new IllegalArgumentException();

        Category category = Category.builder()
                .name(categoryFormDto.getName())
                .user(userRepository.findById(userId).get())
                .upperCategory(upperCategory)
                .build();
        categoryRepository.save(category);
    }

    public void updateCategory(Long categoryId, CategoryController.CategoryFormDto categoryFormDto, Long userId) {
        Category category = categoryRepository.findById(categoryId).get();
        if (category.getUser().getId() != userId)
            throw new AccessDeniedException("");
        if (category.getUpperCategory() == null)
            throw new AccessDeniedException("");

//        Category upperCategory = categoryRepository.findById(categoryFormDto.getUpperId()).get();
//        if (upperCategory.getUser().getId() != userId)
//            throw new AccessDeniedException("");

        category.setName(categoryFormDto.getName());
//        category.setUpperCategory(upperCategory);
        categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId).get();
        if (category.getUser().getId() != userId)
            throw new AccessDeniedException("");
        if (category.getUpperCategory() == null)
            throw new AccessDeniedException("");

        Category newCategory = category.getUpperCategory();
        postRepository.updatePostCategory(category, newCategory);
        categoryRepository.findByUpperCategory(category)
                .stream()
                .forEach(lowerCategory -> postRepository.updatePostCategory(lowerCategory, newCategory));

        categoryRepository.deleteByUpperCategory(category);
        categoryRepository.delete(category);
    }

    public List<CategoryInfoDto> findCategoryInfos(String userAccount) {
        return categoryRepository.findInfoByUserAccount(userAccount);
    }
}
