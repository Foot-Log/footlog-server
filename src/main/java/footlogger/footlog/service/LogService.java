package footlogger.footlog.service;

import footlogger.footlog.converter.LogConverter;
import footlogger.footlog.domain.User;
import footlogger.footlog.domain.jwt.JWTUtil;
import footlogger.footlog.repository.UserRepository;
import footlogger.footlog.web.dto.response.LogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {
    private final UserRepository userRepository;

    @Transactional
    public LogResponseDto.LogListDto getCompletedList(String token) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return LogConverter.toLogList(user);
    }
}
