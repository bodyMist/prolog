package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "LIKES")
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_ID", nullable = false)
    private Long id;

    private LocalDateTime time;


    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
        this.time = LocalDateTime.now();
    }
}
