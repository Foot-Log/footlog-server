package footlogger.footlog.service;

import footlogger.footlog.converter.LogConverter;
import footlogger.footlog.domain.Log;
import footlogger.footlog.domain.User;
import footlogger.footlog.domain.jwt.JWTUtil;
import footlogger.footlog.repository.LogRepository;
import footlogger.footlog.repository.UserRepository;
import footlogger.footlog.web.dto.response.LogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {
    private final UserRepository userRepository;
    private final LogRepository logRepository;

    @Transactional
    public LogResponseDto.LogListDto getCompletedList(String token) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return LogConverter.toLogList(user);
    }

    @Transactional
    public LogResponseDto.LogDetailDto getLogDetail(String token, Long logId){
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Log log = logRepository.findLogById(logId);

        if (log == null) {
            throw new IllegalArgumentException("해당 로그 없음");
        }

        if(!log.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 로그 접근 불가");
        }

        return LogConverter.toLogDetail(log);
    }
}
