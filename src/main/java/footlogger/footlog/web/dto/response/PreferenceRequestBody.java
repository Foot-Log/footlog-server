package footlogger.footlog.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PreferenceRequestBody {
    private List<String> firstKeyword;
    private List<String> secondKeyword;
    private List<String> thirdKeyword;
}
