package kit.prolog.repository.custom;

import kit.prolog.dto.LayoutDto;

import java.util.List;

public interface LayoutCustomRepository {
    List<LayoutDto> findLayoutDetailByMold_Id(Long moldId);
}
