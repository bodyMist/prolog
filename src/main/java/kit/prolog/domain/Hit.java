package kit.prolog.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "HITS")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HIT_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
