package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "DOCUMENTS")
@DiscriminatorValue("7")
@NoArgsConstructor
public class Document extends Layout{
    @Column(nullable = false)
    private String url;

    public Document(String url) {
        this.url = url;
    }
}
