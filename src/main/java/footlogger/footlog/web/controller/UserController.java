package footlogger.footlog.web.controller;

import footlogger.footlog.domain.User;
import footlogger.footlog.service.KakaoService;
import footlogger.footlog.service.UserService;
import footlogger.footlog.web.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final KakaoService kakaoService;
    private final UserService userService;

    @GetMapping("/kakao/callback")
    public UserResponseDto.LoginResultDto kakaoCallBack(@RequestParam String code) {
        User user = kakaoService.processKakaoLogin(code);
        return new UserResponseDto.LoginResultDto(user.getId(), user.getNickname(), user.getProfileImg());
    }

    @GetMapping("/info")
    public UserResponseDto.UserInfoDto getUserInfo(@RequestHeader("Authorization") String token) {
        String tokenWithoutBearer = token.substring(7);
        return userService.getUserInfo(tokenWithoutBearer);

    }
}
