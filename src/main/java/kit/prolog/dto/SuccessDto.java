package kit.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SuccessDto {
    private boolean success;
    private Object data;

    public SuccessDto(boolean success) {
        this.success = success;
    }
}
