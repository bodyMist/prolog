package kit.prolog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "IMAGES")
//@DiscriminatorValue("2")
@IdClass(CompositeKey.class)
@NoArgsConstructor
@ToString
@Getter
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

    public void setLayout(Layout layout, Long sequence) {
        this.layout = layout;
        this.sequence = sequence;
    }
    public Image(String url) {
        this.url = url;
    }
}
