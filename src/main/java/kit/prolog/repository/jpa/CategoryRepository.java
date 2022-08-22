package kit.prolog.repository.jpa;

import kit.prolog.domain.Category;
import kit.prolog.repository.custom.CategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {
    void deleteAllByUser_Id(Long userId);

    int deleteByUpperCategory(Category upperCategory);

    List<Category> findByUpperCategory(Category upperCategory);
}
