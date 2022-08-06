package kit.prolog.dto;

import kit.prolog.domain.Layout;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
@Getter
@Setter
@ToString
public class LayoutDto {
    private Long id;
    private int dtype;
    private double coordinateX;
    private double coordinateY;
    private double width;
    private double height;
    private String explanation;
    private String content;
    private boolean leader;

    private List<String> url;
    private List<String> codes;

    public void addUrl(LayoutDto n){
        url.addAll(n.getUrl());
    }

    // PostPreviewDto 하위 DTO
    public LayoutDto(Long id, int dtype) {
        this.id = id;
        this.dtype = dtype;
    }

    // 카테고리 하위 게시글 조회 - QueryDSL
    public LayoutDto(int dtype, double width, double height, String content,
                     String code, String codeExplanation, String codeType, String url1){
        this.dtype = dtype;
        this.width = width;
        this.height = height;
        this.content = content;
        if (code != null) {
            codes = List.of(code, codeExplanation, codeType);
        }
        if (url == null) {
            this.url = List.of(url1);
        }
    }
    public LayoutDto(LayoutDto node){
        this.dtype = node.getDtype();
        this.width = node.getWidth();
        this.height = node.getHeight();
    }

    // 게시글 작성 API
    public LayoutDto(Long id, String content) {
        this.id = id;
        this.content = content;
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

    // 레이아웃 가져오기
    public LayoutDto(String content) {
        this.content = content;
    }

    public LayoutDto(List<String> url) {
        this.url = url;
    }
    public void addContent(LayoutDto layoutDto){
        this.content = layoutDto.getContent() == null ? null : layoutDto.getContent();
        this.codes = layoutDto.getCodes() == null ? null : layoutDto.getCodes();
        this.url = layoutDto.getUrl() == null ? null : layoutDto.getUrl();
    }

    public boolean getLeader() {
        return leader;
    }
}
