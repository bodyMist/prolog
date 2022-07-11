package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "HYPERLINKS")
@DiscriminatorValue("4")
@NoArgsConstructor
public class Hyperlink extends Layout{
    @Column(nullable = false)
    private String url;

    public Hyperlink(String url) {
        this.url = url;
    }
}
