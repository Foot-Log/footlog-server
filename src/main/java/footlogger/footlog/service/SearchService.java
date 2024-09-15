package footlogger.footlog.service;

import footlogger.footlog.converter.SearchLogConverter;
import footlogger.footlog.domain.SearchLog;
import footlogger.footlog.domain.User;
import footlogger.footlog.repository.UserRepository;
import footlogger.footlog.web.dto.response.SearchLogDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, SearchLog> redisTemplate;

    //검색어 저장 기능
    public void saveRecentSearchLog(User user, String keyword) {

        String now = LocalDateTime.now().toString();

        //key값 : SearchLog + 유저 id 값
        String key = "SearchLog" + user.getId();
        SearchLog value = SearchLog.builder()
                .log(keyword)
                .createdAt(now)
                .build();

        Long size = redisTemplate.opsForList().size(key);

        //10개를 넘을 경우 가장 오래된 데이터 삭제
        if(size == 10) {
            redisTemplate.opsForList().rightPop(key);
        }

        redisTemplate.opsForList().leftPush(key, value);
    }

    //검색 기록 조회
    public List<SearchLogDTO> getRecentSearchLogs(String token) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        String key = "SearchLog" + user.getId();

        List<SearchLog> logs = redisTemplate.opsForList().range(key, 0, 10);

        return logs.stream()
                .map(SearchLogConverter::toDTO)
                .collect(Collectors.toList());
    }

    public Long deleteRecentSearchLog(String token, SearchLogDTO request) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        String key = "SearchLog" + user.getId();
        SearchLog value = SearchLog.builder()
                .log(request.getLog())
                .createdAt(request.getCreatedAt())
                .build();

        long count = redisTemplate.opsForList().remove(key, 1, value);

        if(count == 0) {
            throw new IllegalArgumentException("검색어가 존재하지 않습니다.");
        }

        return count;
    }
}
