package footlogger.footlog.service;

import ch.qos.logback.core.joran.util.beans.BeanDescriptionFactory;
import footlogger.footlog.domain.User;
import footlogger.footlog.domain.jwt.JWTUtil;
import footlogger.footlog.web.dto.response.KakaoTokenResponseDto;
import footlogger.footlog.web.dto.response.KakaoUserInfoResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final UserService userService;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.token_uri}")
    private String tokenUri;

    @Value("${kakao.user_info_uri}")
    private String userInfoUri;


    public KakaoTokenResponseDto getAccessTokenFromKakao(String code) {
        try {
            KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(tokenUri).post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth/token")
                            .queryParam("grant_type", "authorization_code")
                            .queryParam("client_id", clientId)
                            .queryParam("redirect_uri", redirectUri)
                            .queryParam("code", code)
                            .build())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                        log.error("4xx error when requesting access token: {}", clientResponse.statusCode());
                        return Mono.error(new RuntimeException("Invalid Parameter - " + clientResponse.statusCode()));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                        log.error("5xx error when requesting access token: {}", clientResponse.statusCode());
                        return Mono.error(new RuntimeException("Internal Server Error - " + clientResponse.statusCode()));
                    })
                    .bodyToMono(KakaoTokenResponseDto.class)
                    .block();

            return kakaoTokenResponseDto;
        } catch (Exception e) {
            log.error("Error occurred while getting access token from Kakao: ", e);
            throw new RuntimeException("Failed to retrieve access token from Kakao", e);
        }
    }
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {

        KakaoUserInfoResponseDto userInfo = WebClient.create(userInfoUri)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        return userInfo;
    }

    // 사용자 정보로 회원가입 처리
    public void handleUserRegistration(KakaoUserInfoResponseDto userInfo, KakaoTokenResponseDto kakaoTokenResponseDto) {
        Long kakaoId = userInfo.getId();
        Optional<User> existingUser = userService.findByKakaoId(kakaoId);

        if (existingUser.isEmpty()) { // 유저가 존재하지 않으면 회원가입 처리
            User newUser = User.builder()
                    .kakaoId(kakaoId)
                    .nickname(userInfo.getKakaoAccount().getProfile().getNickName())
                    .profileImg(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                    .email(userInfo.getKakaoAccount().getEmail())
                    .level("새싹")
                    .stampCount(0L)
                    .accessToken(kakaoTokenResponseDto.getAccessToken())  // 액세스 토큰 저장
                    .refreshToken(kakaoTokenResponseDto.getRefreshToken())
                    .build();

            userService.save(newUser);
        }else {
            // 이미 존재하는 경우 토큰을 업데이트
            User user = existingUser.get();
            user.setAccessToken(kakaoTokenResponseDto.getAccessToken());
            user.setRefreshToken(kakaoTokenResponseDto.getRefreshToken());
            userService.save(user); // 갱신된 정보 저장
        }
    }
}