package kit.prolog.domain;

import kit.prolog.dto.AttachmentDto;
import kit.prolog.dto.FileDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "ATTACHMENTS")
@NoArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTACHMENT_ID", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, length = 2000)
    private String url;
    @Column(nullable = false)
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 게시글 작성 api
    public Attachment(String id, Post post) {
        this.id = Long.parseLong(id);
        this.post = post;
    }
    public Attachment(FileDto file){
        this.name = file.getSavedName();
        this.url = file.getPath();
        this.extension = file.getType();
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
