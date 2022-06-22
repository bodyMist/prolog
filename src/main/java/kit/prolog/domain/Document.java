package kit.prolog.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "DOCUMENTS")
@DiscriminatorValue("7")
public class Document extends Layout{
    @Column(nullable = false)
    private String url;
}
