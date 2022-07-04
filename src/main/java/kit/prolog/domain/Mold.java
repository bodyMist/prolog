package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "MOLDS")
@NoArgsConstructor
public class Mold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOLD_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "mold")
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "mold", cascade = CascadeType.ALL)
    private List<Layout> layouts = new ArrayList<>();

    public Mold(Long id) {
        this.id = id;
    }
    public Mold(String name, User user) {
        this.name = name;
        this.user = user;
    }
}
