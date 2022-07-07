package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "TAGS")
@NoArgsConstructor
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
