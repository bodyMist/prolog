package kit.prolog.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "VIDEOS")
@DiscriminatorValue("6")
public class Video extends Layout{
    @Column(nullable = false)
    private String url;
}
