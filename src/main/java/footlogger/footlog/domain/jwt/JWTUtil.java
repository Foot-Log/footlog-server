package footlogger.footlog.domain.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTUtil {
    private final TokenProvider tokenProvider;
    private final JWTProperties jwtProperties;

    public String createAccessToken(String email) {
        return tokenProvider.createToken(email, jwtProperties.getAccessTokenValidity());
    }

    public String createRefreshToken(String email) {
        return tokenProvider.createToken(email, jwtProperties.getRefreshTokenValidity());
    }

    public String getEmailFromToken(String token) {
        return tokenProvider.getEmailFromToken(token);
    }

    // public String getUserIdFromToken(String token) 추후 구현
}
