package footlogger.footlog.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserResponseDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LoginResultDto {
        private Long id;
        private String nickname;
        private String profileImg;
    }
}
