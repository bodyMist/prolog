package kit.prolog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDto {
    private String originalName;
    private String savedName;
    private String type;
    private String path;
    private Long size;
}
