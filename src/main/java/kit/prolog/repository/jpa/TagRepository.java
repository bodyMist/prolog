package kit.prolog.repository.jpa;

import kit.prolog.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // 검색어가 포함된 태그 이름 모두 조회
    List<Tag> findAllByNameLike(String name);
    // 태그 여부 확인
    boolean existsByName(String name);
    // 태그 조회(name은 unique key)
    Optional<Tag> findByName(String name);
}
