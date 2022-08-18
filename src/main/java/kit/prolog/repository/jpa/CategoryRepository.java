package kit.prolog.repository.jpa;

import kit.prolog.domain.Category;
import kit.prolog.repository.custom.CategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {
    void deleteAllByUser_Id(Long userId);
}
