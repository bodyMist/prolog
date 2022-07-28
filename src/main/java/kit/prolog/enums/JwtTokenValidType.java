package kit.prolog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtTokenValidType {
    ACCESS_TOKEN_EXPIRATION_TIME(1000L * 60 * 30), // 1초 > 1분 > 30분
    REFRESH_TOKEN_EXPIRATION_TIME(1000L * 60 * 60 * 24 * 7), // 7일
    REISSUE_EXPIRATION_TIME(1000L * 60 * 60 * 24 * 3); // 3일

    private Long time;
}
