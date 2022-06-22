package kit.prolog.domain;

import javax.persistence.*;

@Entity(name = "TAGS")
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long id;
    @Column(nullable = false)
    private String name;
}
