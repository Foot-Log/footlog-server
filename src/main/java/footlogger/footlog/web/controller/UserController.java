package footlogger.footlog.web.controller;

import footlogger.footlog.domain.User;
import footlogger.footlog.payload.ApiResponse;
import footlogger.footlog.service.KakaoService;
import footlogger.footlog.service.UserService;
import footlogger.footlog.web.dto.response.KakaoTokenResponseDto;
import footlogger.footlog.web.dto.response.KakaoUserInfoResponseDto;
import footlogger.footlog.web.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

import static footlogger.footlog.payload.code.status.ErrorStatus._INTERNAL_SERVER_ERROR;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final KakaoService kakaoService;
    private final UserService userService;

    @CrossOrigin("*")
    @Operation(summary = "카카오 로그인 및 회원 가입")
    @GetMapping("/kakao/callback")
    public ApiResponse<UserResponseDto.LoginResultDto> callback(@RequestParam("code") String code, @RequestParam("redirect_uri") String redirectUri) throws IOException {
        try {

            String decodedUri = URLDecoder.decode(redirectUri, "UTF-8");

            KakaoTokenResponseDto kakaoTokenResponseDto = kakaoService.getAccessTokenFromKakao(code, decodedUri);
            KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoTokenResponseDto.getAccessToken());
//
            UserResponseDto.LoginResultDto loginResultDto = kakaoService.handleUserLogin(userInfo);

//            kakaoService.handleUserRegistration(userInfo, kakaoTokenResponseDto);
//
//            UserResponseDto.LoginResultDto loginResultDto = UserResponseDto.LoginResultDto.builder()
//                    .accessToken(kakaoTokenResponseDto.getAccessToken())
//                    .refreshToken(kakaoTokenResponseDto.getAccessToken())
//                    .build();
            log.info("Login result: {}", loginResultDto);
            return ApiResponse.onSuccess(loginResultDto);
        } catch (Exception e) {
            return ApiResponse.onFailure(_INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "유저 정보 조회")
    @GetMapping("/info")
    public ApiResponse<UserResponseDto.UserInfoDto> getUserInfo(@RequestHeader("Authorization") String token) {
        String tokenWithoutBearer = token.substring(7);
        UserResponseDto.UserInfoDto response = userService.getUserInfo(tokenWithoutBearer);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestHeader("Authorization") String token) {
        String tokenWithoutBearer = token.substring(7);
        try {
            userService.deleteUser(tokenWithoutBearer);
            return new ResponseEntity<>(ApiResponse.onSuccess(null), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(ApiResponse.onFailure(_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
