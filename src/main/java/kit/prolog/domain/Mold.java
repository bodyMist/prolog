package kit.prolog.domain;

import javax.persistence.*;

@Entity(name = "MOLDS")
public class Mold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOLD_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
