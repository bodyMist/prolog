package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "IMAGES")
@DiscriminatorValue("2")
@IdClass(CompositeKey.class)
@NoArgsConstructor
public class Image{
    @Id
    @ManyToOne
    @JoinColumn(name = "LAYOUT_ID", nullable = false)
    private Layout layout;

    @Id
    @Column(name = "SEQUENCE", nullable = false)
    private Long sequence;

    @Column(nullable = false, length = 2000)
    private String url;

    public Image(String url) {
        this.url = url;
    }
}
