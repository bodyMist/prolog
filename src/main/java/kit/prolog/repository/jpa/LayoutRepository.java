package kit.prolog.repository.jpa;

import kit.prolog.domain.Layout;
import kit.prolog.dto.LayoutDto;
import kit.prolog.repository.custom.LayoutCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LayoutRepository extends JpaRepository<Layout, Long>, LayoutCustomRepository {
    @Query("SELECT new kit.prolog.dto.LayoutDto(l.id, l.dtype, l.coordinateX, l.coordinateY, l.width, l.height) " +
            "FROM LAYOUTS l WHERE l.mold.id = :moldId")
    List<LayoutDto> findByMold_Id(Long moldId);
    List<Layout> findLayoutByMold_Id(Long moldId);

    @Query("SELECT l FROM LAYOUTS l WHERE l.id = :id")
    Optional<Layout> findLayoutById(Long id);
}
