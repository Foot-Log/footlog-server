package footlogger.footlog.service;

import footlogger.footlog.converter.UserConverter;
import footlogger.footlog.domain.User;
import footlogger.footlog.domain.jwt.JWTUtil;
import footlogger.footlog.repository.LogRepository;
import footlogger.footlog.repository.UserRepository;
import footlogger.footlog.web.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final RedisTemplate<String, Long> courseRedisTemplate;
    private final RedisTemplate<String, String> searchRedisTemplate;

    public User findByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId).orElse(null);
    }

    public void save(User user){
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDto.UserInfoDto getUserInfo(String token) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return UserConverter.toUserInfo(user);
    }

    @Transactional
    public void deleteUser(String token) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        //redis 서버에 저장된 정보 삭제
        courseRedisTemplate.delete("Course" + user.getId());
        searchRedisTemplate.delete("SearchLog" + user.getId());

        logRepository.deleteByUser(user);
        userRepository.delete(user);
    }
}
