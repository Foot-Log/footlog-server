package footlogger.footlog.web.controller;

import footlogger.footlog.domain.User;
import footlogger.footlog.service.KakaoService;
import footlogger.footlog.web.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final KakaoService kakaoService;

    @GetMapping("/kakao/callback")
    public UserResponseDto.LoginResultDto kakaoCallBack(@RequestParam String code) {
        User user = kakaoService.processKakaoLogin(code);
        return new UserResponseDto.LoginResultDto(user.getId(), user.getNickname(), user.getProfileImg());
    }
}
