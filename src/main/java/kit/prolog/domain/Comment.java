package kit.prolog.domain;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String context;
    @Column(nullable = false)
    private LocalDateTime time;
    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean block;      // false : 삭제X, true : 삭제됨

    @OneToOne(fetch = FetchType.LAZY)
    private Comment upperComment;       // 상위 댓글(자기참조)

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
