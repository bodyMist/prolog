package kit.prolog.dto;

import lombok.AllArgsConstructor;

/*
* 레이아웃 틀 조회 API 용 DTO 클래스
* 시나리오 : 글 작성 -> 회원이 저장한 모든 레이아웃틀 조회
* */
@AllArgsConstructor
public class MoldDto {
    Long id;
    String name;
}
