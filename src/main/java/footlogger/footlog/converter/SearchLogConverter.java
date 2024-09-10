package footlogger.footlog.converter;

import footlogger.footlog.domain.SearchLog;
import footlogger.footlog.web.dto.response.SearchLogDTO;

import java.util.List;

public class SearchLogConverter {

    public static SearchLogDTO toDTO(SearchLog searchLog) {
        return SearchLogDTO.builder()
                .log(searchLog.getLog())
                .CreatedAt(searchLog.getCreatedAt())
                .build();
    }
}
