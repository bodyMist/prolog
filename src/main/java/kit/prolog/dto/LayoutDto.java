package kit.prolog.dto;

import kit.prolog.domain.Layout;
import lombok.Getter;
import lombok.ToString;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
@Getter
@ToString
public class LayoutDto {
    private Long id;
    private int dtype;
    private double coordinateX;
    private double coordinateY;
    private double width;
    private double height;
    private String context;

    // PostPreviewDto 하위 DTO
    public LayoutDto(Long id, int dtype) {
        this.id = id;
        this.dtype = dtype;
    }

    // 게시글 작성 API
    public LayoutDto(Long id, String context) {
        this.id = id;
        this.context = context;
    }

    // 레이아웃 틀 하위 레이아웃 리스트 조회 API
    public LayoutDto(Long id, int dtype, double coordinateX, double coordinateY, double width, double height) {
        this.id = id;
        this.dtype = dtype;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.width = width;
        this.height = height;
    }

    // 레이아웃 작성 API
    public LayoutDto(int dtype, double coordinateX, double coordinateY, double width, double height) {
        this.dtype = dtype;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.width = width;
        this.height = height;
    }

    // 레이아웃 작성 service layer
    public LayoutDto(Layout layout){
        this.dtype = layout.getDtype();
        this.coordinateX = layout.getCoordinateX();
        this.coordinateY = layout.getCoordinateY();
        this.width = layout.getWidth();
        this.height = layout.getHeight();
    }
}
