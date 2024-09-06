package footlogger.footlog.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    private static final String CORS_URL_PATTERN = "/**";
    private static final String CORS_URL = "http://localhost:3000"; // 허용할 프론트엔드 URL
    private static final String CORS_METHOD = "*";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(CORS_URL_PATTERN)
                .allowedOrigins("http://localhost:3000", "http://43.201.255.209:8080")
                .allowedMethods(CORS_METHOD) // 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("Authorization") // 필요한 경우 노출할 헤더
                .allowCredentials(true); // 쿠키나 인증 정보 사용 허용
    }
}
