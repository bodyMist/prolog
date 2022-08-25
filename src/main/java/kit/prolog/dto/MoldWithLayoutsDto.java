package kit.prolog.dto;

import kit.prolog.domain.Mold;
import lombok.Data;

import java.util.List;

@Data
public class MoldWithLayoutsDto {
    private Long layoutId;
    private String title;
    private List<LayoutDto> layouts;

    public MoldWithLayoutsDto(Mold mold, List<LayoutDto> layouts) {
        this.layoutId = mold.getId();
        this.title = mold.getName();
        this.layouts = layouts;
    }
}
