package kit.prolog.dto;

import kit.prolog.domain.Attachment;
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

    public FileDto(Attachment attachment) {
        this.savedName = attachment.getName();
        this.type = attachment.getExtension();
        this.path = attachment.getUrl();
    }
}
