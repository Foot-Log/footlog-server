package footlogger.footlog.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class LogResponseDto {

//    @Builder
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class LogListDto {
//        private List<LogNameDto> logList;
//    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogDto {
        private Long logId;
        private String address;
        private String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogDetailDto {
        private Long logId;
        private String address;
        private String logContent;
        private List<String> photos;
    }
}
