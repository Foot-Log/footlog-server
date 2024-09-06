package footlogger.footlog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String CORS_URL_PATTERN = "/**";
    private static final String CORS_METHOD = "*";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(CORS_URL_PATTERN)
                .allowedOrigins("*")
                .allowedMethods(CORS_METHOD) // 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("Authorization"); // 필요한 경우 노출할 헤더
                //.allowCredentials(true); // 쿠키나 인증 정보 사용 허용
    }
}
