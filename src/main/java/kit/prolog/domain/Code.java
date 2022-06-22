package kit.prolog.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "CODES")
@DiscriminatorValue("3")
public class Code extends Layout{
    @Column(nullable = false)
    private String code;
}
