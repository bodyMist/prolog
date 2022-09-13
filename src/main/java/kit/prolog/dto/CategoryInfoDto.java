package kit.prolog.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDto {
    private Long id;
    private String name;
    private Long count;
    private List<CategoryInfoDto> child;
}
