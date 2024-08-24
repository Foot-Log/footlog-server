package footlogger.footlog.domain.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
public class JWTProperties {
    private String secret;
    private Long accessTokenValidity;
    private Long refreshTokenValidity;
}
