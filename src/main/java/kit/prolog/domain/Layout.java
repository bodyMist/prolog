package kit.prolog.domain;

import kit.prolog.dto.LayoutDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity(name = "LAYOUTS")
@NoArgsConstructor
@AllArgsConstructor
public class Layout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LAYOUT_ID", nullable = false)
    protected Long id;

    private Double coordinateX;
    private Double coordinateY;
    private Double width;
    private Double height;
    private String explanation;

    @Column(name = "dtype")
    protected Integer dtype;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mold mold;

    public Layout(Long id) {
        this.id = id;
    }

    public Layout(LayoutDto layoutDto) {
        this.coordinateX = layoutDto.getCoordinateX();
        this.coordinateY = layoutDto.getCoordinateY();
        this.width = layoutDto.getWidth();
        this.height = layoutDto.getHeight();
        this.dtype = layoutDto.getDtype();
    }

    public Layout(Long id, Double coordinateX, Double coordinateY, Double width, Double height, String explanation, int dtype) {
        this.id = id;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.width = width;
        this.height = height;
        this.explanation = explanation;
        this.dtype = dtype;
    }
}
