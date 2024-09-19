package footlogger.footlog.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SigunguCodeDTO {
    private Long sigunguId;
    private String sigunguName;
    private String withArea;
}
