package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
 * PostDetailDto를 위한 부분 DTO
 * */
@AllArgsConstructor
@Setter
@Getter
public class LikeDto {
    private int count;
    private boolean exist;

    public LikeDto(int likeCount) {
        this.count = likeCount;
        this.exist = false;
    }
}
