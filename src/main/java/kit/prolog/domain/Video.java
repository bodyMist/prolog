package kit.prolog.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "VIDEOS")
@DiscriminatorValue("6")
@NoArgsConstructor
public class Video extends Layout{
    @Column(nullable = false)
    private String url;

    public Video(String url) {
        this.url = url;
    }
}
