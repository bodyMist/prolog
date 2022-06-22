package kit.prolog.domain;

import javax.persistence.*;

@Entity(name = "POSTS_AND_TAGS")
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_AND_TAG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;
}
