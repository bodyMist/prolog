package kit.prolog.dto;

import kit.prolog.domain.Attachment;
import lombok.Getter;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
@Getter
public class AttachmentDto {
    private Long id;
    private String name;
    private String url;

    public AttachmentDto(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public AttachmentDto(Attachment attachment) {
        this.id = attachment.getId();
        this.name = attachment.getName();
        this.url = attachment.getUrl();
    }
}
