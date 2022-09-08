package kit.prolog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {
    private String originalName;
    private String savedName;
    private String type;
    private String path;
    private Long size;
}
