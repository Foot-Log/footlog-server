package footlogger.footlog.web.controller;

import footlogger.footlog.domain.User;
import footlogger.footlog.payload.ApiResponse;
import footlogger.footlog.service.KakaoService;
import footlogger.footlog.service.UserService;
import footlogger.footlog.web.dto.response.KakaoTokenResponseDto;
import footlogger.footlog.web.dto.response.KakaoUserInfoResponseDto;
import footlogger.footlog.web.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static footlogger.footlog.payload.code.status.ErrorStatus._INTERNAL_SERVER_ERROR;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final KakaoService kakaoService;
    private final UserService userService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        try {
            KakaoTokenResponseDto kakaoTokenResponseDto = kakaoService.getAccessTokenFromKakao(code);
            KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoTokenResponseDto.getAccessToken());
            kakaoService.handleUserRegistration(userInfo, kakaoTokenResponseDto);

            UserResponseDto.LoginResultDto loginResultDto = UserResponseDto.LoginResultDto.builder()
                    .accessToken(kakaoTokenResponseDto.getAccessToken())
                    .refreshToken(kakaoTokenResponseDto.getAccessToken())
                    .build();
            return new ResponseEntity<>(ApiResponse.onSuccess(loginResultDto), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error during Kakao callback: ", e);
            return new ResponseEntity<>(ApiResponse.onFailure(_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/info")
    public UserResponseDto.UserInfoDto getUserInfo(@RequestHeader("Authorization") String token) {
        String tokenWithoutBearer = token.substring(7);
        return userService.getUserInfo(tokenWithoutBearer);
    }
}
