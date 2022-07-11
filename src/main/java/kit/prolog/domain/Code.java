package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "CODES")
@DiscriminatorValue("3")
@NoArgsConstructor
public class Code extends Layout{
    @Column(nullable = false)
    private String code;

    public Code(String code) {
        this.code = code;
    }
}
