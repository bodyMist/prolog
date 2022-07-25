package kit.prolog.repository.jpa;

import kit.prolog.domain.Code;
import kit.prolog.domain.CompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CodeRepository extends JpaRepository<Code, CompositeKey> {
    @Modifying
    @Query(value = "INSERT INTO Codes(layout_id, sequence, code, code_explanation) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void saveCode(Long layoutId, Long sequence, String code, String codeExplanation);
}
