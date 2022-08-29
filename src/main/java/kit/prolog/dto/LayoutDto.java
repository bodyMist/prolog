package kit.prolog.dto;

import kit.prolog.domain.Layout;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
@Getter
@Setter
@NoArgsConstructor
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

    private List<String> url = new ArrayList<>();
    private List<String> codes;

    public void addUrl(LayoutDto n){
        this.url.addAll(n.getUrl());
    }

    public LayoutDto(LinkedHashMap<String, Object> json) {
        this.id = json.get("id") == null
                ? null : Long.parseLong(json.get("id").toString());
        this.dtype = Integer.parseInt(json.get("type").toString());
        this.coordinateX = Double.parseDouble(json.get("coordinateX").toString());
        this.coordinateY = Double.parseDouble(json.get("coordinateY").toString());
        this.width = Double.parseDouble(json.get("width").toString());
        this.height = Double.parseDouble(json.get("height").toString());

        this.explanation = json.get("explanation") == null
                ? "" : json.get("explanation").toString();

        this.content = json.get("content") == null
                ? "" : json.get("content").toString();

        if(json.get("images") != null) {
            ((List<LinkedHashMap<String, String>>) json.get("images"))
                    .forEach(image -> this.url.add(image.get("url")));
        }
        this.codes = json.get("codes") == null
                ? null : ((List<String>) json.get("codes"))
                .stream().map(Objects::toString).collect(Collectors.toList());
    }

    // PostPreviewDto 하위 DTO
    public LayoutDto(Long id, int dtype) {
        this.id = id;
        this.dtype = dtype;
    }

    // 카테고리 하위 게시글 조회 - QueryDSL
    public LayoutDto(Integer dtype, Double width, Double height, String explanation, String content,
                     String code, String codeExplanation, String codeType, String url1){
        this.dtype = dtype;
        this.width = width;
        this.height = height;
        this.explanation = explanation;
        this.content = content;
        if (code != null) {
            this.codes = List.of(code, codeExplanation, codeType);
        }
        if (url1 != null) {
            this.url.add(url1);
        }
    }
    public LayoutDto(LayoutDto node){
        this.dtype = node.getDtype();
        this.width = node.getWidth();
        this.height = node.getHeight();
        this.content = node.getContent();
        this.explanation = node.getExplanation();
        this.codes = node.getCodes();
        this.url.addAll(node.getUrl());
    }

    // 게시글 작성 API
    public LayoutDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    // 게시글 상세조회 API
    public LayoutDto(Layout layout, String context, String code,
                     String codeExplanation, String codeType, String url, Boolean main){
        this.id = layout.getId();
        this.dtype = layout.getDtype();
        this.coordinateX = layout.getCoordinateX();
        this.coordinateY = layout.getCoordinateY();
        this.width = layout.getWidth();
        this.height = layout.getHeight();
        this.explanation = layout.getExplanation();
        this.content = context;
        this.leader = main;

        if (code != null) {
            this.codes = List.of(code, codeExplanation, codeType);
        }
        if (url != null) {
            this.url.add(url);
        }
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


    // 레이아웃 작성 service layer
    public LayoutDto(Layout layout){
        this.id = layout.getId();
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
