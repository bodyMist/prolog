package kit.prolog.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "COMMENTS")
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String context;
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime time;
    @Column(nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean block = false;      // false : 삭제X, true : 삭제됨

    @OneToOne(fetch = FetchType.LAZY)
    private Comment upperComment;       // 상위 댓글(자기참조)

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
