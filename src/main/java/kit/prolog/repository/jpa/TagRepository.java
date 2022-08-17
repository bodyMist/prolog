package kit.prolog.repository.jpa;

import kit.prolog.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // 검색어가 포함된 태그 이름 모두 조회
    @Query("SELECT t FROM TAGS t WHERE t.name LIKE :name%")
    List<Tag> findByNameStartingWith(String name);
    // 태그 조회(name은 unique key)
    Optional<Tag> findByName(String name);
}
