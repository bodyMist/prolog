package kit.prolog.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "HYPERLINKS")
@DiscriminatorValue("4")
public class Hyperlink extends Layout{
    @Column(nullable = false)
    private String url;
}
