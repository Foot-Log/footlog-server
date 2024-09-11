package footlogger.footlog.utils;

import footlogger.footlog.domain.Course;
import footlogger.footlog.web.dto.response.PreferenceRequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RecommendSystem {
    @Value("${FLASK_BaseURL}")
    private String baseUrl;

    public List<Long> getRecommendations(PreferenceRequestBody request) {
        WebClient webClient = WebClient.builder()

                .baseUrl(baseUrl)
                .build();

        Map<String, Object> response =
                webClient
                        .post()
                        .uri(uriBuilder -> uriBuilder.path("/recommend/course").build())
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                        .block();

        // "course_id" 필드에서 값을 추출
        if (response != null && response.containsKey("course_id")) {
            List<String> courseIds = (List<String>) response.get("course_id");

            // List<String>을 List<Long>으로 변환
            return courseIds.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("course_id를 응답에서 찾을 수 없습니다.");
        }
    }
}
