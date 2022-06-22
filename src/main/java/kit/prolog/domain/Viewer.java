package kit.prolog.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "VIEWERS")
@DiscriminatorValue("7")
public class Viewer extends Layout{
    @Column(nullable = false)
    private String url;
}
