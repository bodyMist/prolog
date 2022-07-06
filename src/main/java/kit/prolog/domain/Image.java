package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "IMAGES")
@DiscriminatorValue("2")
@NoArgsConstructor
public class Image extends Layout{
    @Column(nullable = false)
    private String url;

    public Image(String url) {
        this.url = url;
    }
}
