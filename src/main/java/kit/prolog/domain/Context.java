package kit.prolog.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "CONTEXTS")
@DiscriminatorValue("1")
public class Context extends Layout{
    @Column(nullable = false)
    private String text;
}
