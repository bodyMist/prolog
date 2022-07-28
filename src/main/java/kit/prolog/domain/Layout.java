package kit.prolog.domain;

import kit.prolog.dto.LayoutDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity(name = "LAYOUTS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@NoArgsConstructor
@AllArgsConstructor
@OnDelete(action = OnDeleteAction.CASCADE)
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
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private Boolean main = false;       // 대표 레이아웃

    @Column(name = "dtype", insertable = false, updatable = false)
    protected int dtype;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mold mold;

    public Layout(Long id) {
        this.id = id;
    }

    public Layout(LayoutDto layoutDto, Mold mold) {
        this.coordinateX = layoutDto.getCoordinateX();
        this.coordinateY = layoutDto.getCoordinateY();
        this.width = layoutDto.getWidth();
        this.height = layoutDto.getHeight();
        this.dtype = layoutDto.getDtype();
        this.mold = mold;
    }

    public Layout(Long id, Double coordinateX, Double coordinateY, Double width, Double height, String explanation, Boolean main, int dtype) {
        this.id = id;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.width = width;
        this.height = height;
        this.explanation = explanation;
        this.main = main;
        this.dtype = dtype;
    }

    public void setMold(Mold mold) {
        this.mold = mold;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
