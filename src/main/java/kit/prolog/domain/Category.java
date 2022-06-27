package kit.prolog.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CATEGORIES")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();
}