package kit.prolog.repository.jpa;

import kit.prolog.domain.CompositeKey;
import kit.prolog.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, CompositeKey> {
    @Modifying
    @Query(value = "INSERT INTO Images(layout_id, sequence , url) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void saveImage(Long layoutId, Long sequence, String url);
}
