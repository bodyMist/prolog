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
    private String content;

    public LayoutDto(Long id, int dtype) {
        this.id = id;
        this.dtype = dtype;
    }
}
