package footlogger.footlog.payload.code;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReasonDTO {
    private final String code;
    private final String message;

    public ReasonDTO(String code, String message){
        this.code = code;
        this.message = message;
    }
}
