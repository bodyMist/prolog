package kit.prolog.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDto {
    private Long id;
    private String name;
    private Long count;
}
