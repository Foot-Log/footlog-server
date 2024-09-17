package footlogger.footlog.converter;

import footlogger.footlog.domain.SearchLog;
import footlogger.footlog.web.dto.response.SearchLogDTO;

import java.util.List;

public class SearchLogConverter {

    public static SearchLogDTO toDTO(String keyword) {
        return SearchLogDTO.builder()
                .log(keyword)
                .build();
    }
}
