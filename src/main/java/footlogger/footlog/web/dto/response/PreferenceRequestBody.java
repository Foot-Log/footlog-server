package footlogger.footlog.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PreferenceRequestBody {
    private String firstKeyword;
    private String secondKeyword;
    private String thirdKeyword;
}
