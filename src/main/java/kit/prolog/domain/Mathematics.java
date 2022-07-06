package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "MATHEMATICS")
@DiscriminatorValue("5")
@NoArgsConstructor
public class Mathematics extends Layout{
    @Column(nullable = false)
    private String context;

    public Mathematics(String context) {
        this.context = context;
    }
}
