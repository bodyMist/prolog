package kit.prolog.repository.jpa;

import kit.prolog.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    public List<Tag> findAllByNameLike(String name);
}
