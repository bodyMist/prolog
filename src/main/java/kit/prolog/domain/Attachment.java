package kit.prolog.domain;

import kit.prolog.dto.AttachmentDto;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "ATTACHMENTS")
@NoArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTACHMENT_ID", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 게시글 작성 api
    public Attachment(AttachmentDto dto, Post post) {
        this.id = dto.getId();
        this.url = dto.getUrl();
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
