package kit.prolog.dto;

import kit.prolog.domain.Attachment;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

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

    public AttachmentDto(LinkedHashMap<String, String> json) {
        this.id = Long.parseLong(json.get("id"));
        this.name = json.get("name");
        this.url = json.get("url");
    }
    public AttachmentDto(Attachment attachment) {
        this.id = attachment.getId();
        this.name = attachment.getName();
        this.url = attachment.getUrl();
    }
}
