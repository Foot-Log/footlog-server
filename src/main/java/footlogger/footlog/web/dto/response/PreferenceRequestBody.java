package footlogger.footlog.web.dto.response;

import lombok.Builder;

@Builder
public class PreferenceRequestBody {
    private String firstKeyword;
    private String secondKeyword;
    private String thirdKeyword;
}
