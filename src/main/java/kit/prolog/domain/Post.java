package kit.prolog.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "POSTS")
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    private Mold mold;

    @OneToMany(mappedBy = "post")
    private List<Like> likes = new ArrayList<>();

    public Post(Long id) {
        this.id = id;
    }

    public void setMold(Mold mold) {
        this.mold = mold;
    }

    public Post(String title, LocalDateTime time, User user, Category category) {
        this.title = title;
        this.time = time;
        this.user = user;
        this.category = category;
    }
}
