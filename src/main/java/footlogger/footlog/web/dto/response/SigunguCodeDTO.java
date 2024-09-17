package footlogger.footlog.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SigunguCodeDTO {
    private int sigunguCode;
    private int areaCode;
    private String sigunguName;
}
