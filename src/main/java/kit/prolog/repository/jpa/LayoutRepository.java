package kit.prolog.repository.jpa;

import kit.prolog.domain.Layout;
import kit.prolog.dto.LayoutDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    @Query("SELECT new kit.prolog.dto.LayoutDto(l.id, l.dtype, l.coordinateX, l.coordinateY, l.width, l.height) " +
            "FROM LAYOUTS l WHERE l.mold.id = :moldId")
    List<LayoutDto> findByMold_Id(Long moldId);
}
