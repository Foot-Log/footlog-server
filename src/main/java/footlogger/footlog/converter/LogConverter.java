package footlogger.footlog.converter;

import footlogger.footlog.domain.Log;
import footlogger.footlog.domain.LogPhoto;
import footlogger.footlog.domain.User;
import footlogger.footlog.web.dto.response.LogResponseDto;

import java.util.stream.Collectors;

public class LogConverter {
    public static LogResponseDto.LogListDto toLogList(User user) {
        return LogResponseDto.LogListDto.builder()
                .logList(user.getLogList().stream()
                        .map(LogConverter::toLogName).collect(Collectors.toList()))
                .build();
    }
    public static LogResponseDto.LogNameDto toLogName(Log log){
        return LogResponseDto.LogNameDto.builder()
                .logId(log.getId())
                .address(log.getCourse().getAddress())
                .build();
    }
    public static LogResponseDto.LogDetailDto toLogDetail(Log log) {
        return LogResponseDto.LogDetailDto.builder()
                .logId(log.getId())
                .address(log.getCourse().getAddress())
                .logContent(log.getLogContent())
                .photos(log.getPhotos().stream().map(LogPhoto::getUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}
