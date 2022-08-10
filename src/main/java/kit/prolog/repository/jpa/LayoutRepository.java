package kit.prolog.repository.jpa;

import kit.prolog.domain.Layout;
import kit.prolog.dto.LayoutDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    @Query("SELECT new kit.prolog.dto.LayoutDto(l.id, l.dtype, l.coordinateX, l.coordinateY, l.width, l.height) " +
            "FROM LAYOUTS l WHERE l.mold.id = :moldId")
    List<LayoutDto> findLayoutDtoByMold_Id(Long moldId);

    List<Layout> findByMold_Id(Long moldId);

    boolean deleteAllByMold_Id(Long moldId);

    @Query("SELECT l FROM LAYOUTS l WHERE l.id = :id")
    Optional<Layout> findLayoutById(Long id);

    @Query("SELECT new kit.prolog.domain.Layout(l.id, l.coordinateX, l.coordinateY, l.width, l.height, l.explanation, l.dtype) FROM LAYOUTS l WHERE l.id = :id")
    Optional<Layout> findById(Long id);
}
