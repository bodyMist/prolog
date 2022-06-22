package kit.prolog.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "MATHEMATICS")
@DiscriminatorValue("5")
public class Mathematics extends Layout{
    @Column(nullable = false)
    private String context;
}
