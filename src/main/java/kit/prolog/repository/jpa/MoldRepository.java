package kit.prolog.repository.jpa;

import kit.prolog.domain.Mold;
import kit.prolog.dto.MoldDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoldRepository extends JpaRepository<Mold, Long> {
    @Query("SELECT new kit.prolog.dto.MoldDto(m.id, m.name) FROM MOLDS m WHERE m.user.id = :userId")
    List<MoldDto> findByUser_Id(@Param("userId") Long userId);
    void deleteByUser_Id(Long userId);
}
