package kit.prolog.domain;

import javax.persistence.*;

@Entity(name = "ATTACHMENTS")
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
}
