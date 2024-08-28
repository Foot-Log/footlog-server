package footlogger.footlog.domain.jwt;

import footlogger.footlog.domain.User;
import footlogger.footlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTUtil {
    private final TokenProvider tokenProvider;
    private final JWTProperties jwtProperties;
    private final UserRepository userRepository;

    public String createAccessToken(String email) {
        return tokenProvider.createToken(email, jwtProperties.getAccessTokenValidity());
    }

    public String createRefreshToken(String email) {
        return tokenProvider.createToken(email, jwtProperties.getRefreshTokenValidity());
    }

    public String getEmailFromToken(String token) {
        return tokenProvider.getEmailFromToken(token);
    }

    public Long getUserIdFromToken(String token) {
        String email = getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 계정입니다."));
        return user.getKakaoId();
    }
}
