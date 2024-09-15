package footlogger.footlog.converter;

import footlogger.footlog.domain.Log;
import footlogger.footlog.domain.LogPhoto;
import footlogger.footlog.domain.User;
import footlogger.footlog.web.dto.response.LogResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class LogConverter {
    public static List<LogResponseDto.LogDto> toLogList(User user) {
        return user.getLogList().stream()
                .map(LogConverter::toLog)
                .collect(Collectors.toList());
    }
    public static LogResponseDto.LogDto toLog(Log log){
        return LogResponseDto.LogDto.builder()
                .logId(log.getId())
                .address(log.getCourse().getAddress())
                .name(log.getCourse().getName())
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
