package kit.prolog.repository.jpa;

import kit.prolog.domain.Mold;
import kit.prolog.dto.MoldDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoldRepository extends JpaRepository<Mold, Long> {
    List<MoldDto> findByUser_Id(Long userId);
}
