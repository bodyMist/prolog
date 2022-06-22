package kit.prolog.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "IMAGES")
@DiscriminatorValue("2")
public class Image extends Layout{
    @Column(nullable = false)
    private String url;
}
