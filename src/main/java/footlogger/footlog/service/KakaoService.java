package footlogger.footlog.service;

import footlogger.footlog.domain.User;
import footlogger.footlog.domain.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public String getAccessToken(String code) {
        // 카카오 서버로부터 액세스 토큰을 요청하는 메서드
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, request, Map.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        }

        throw new RuntimeException("Failed to get access token from Kakao");

    }

    public Map<String, Object> getUserInfo(String accessToken) {
        // 액세스 토큰으로 사용자 정보를 요청하는 메서드
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        throw new RuntimeException("Failed to get user info from Kakao");

    }

    public User processKakaoLogin(String code) {
        // 카카오 로그인 처리 메서드
        String accessToken = getAccessToken(code);
        Map<String, Object> userInfo = getUserInfo(accessToken);

        // 카카오에서 받아온 사용자 정보 추출
        String email = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("email");
        Long kakaoId = ((Number) userInfo.get("id")).longValue();
        String nickname = (String) ((Map<String, Object>) userInfo.get("properties")).get("nickname");
        String profileImg = (String) ((Map<String, Object>) userInfo.get("properties")).get("profile_image");

        // DB에서 사용자 정보를 확인
        Optional<User> optionalUser = userService.findByKakaoId(kakaoId);
        User user;

        if(optionalUser.isPresent()) {
            // 사용자가 존재하면 그 사용자 정보를 가져옴
            user = optionalUser.get();

        }
         else {
            // 사용자가 없으면 회원가입 처리
             user = User.builder()
                     .kakaoId(kakaoId)
                     .email(email)
                     .nickname(nickname)
                     .profileImg(profileImg)
                     .build();
        }

         String accessTokenJwt = jwtUtil.createAccessToken(email);
         String refreshTokenJwt = jwtUtil.createRefreshToken(email);

        user.setRefreshToken(refreshTokenJwt);
         userService.save(user);

         return user;
    }

}
