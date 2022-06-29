package kit.prolog.dto;

import lombok.ToString;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
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
}
