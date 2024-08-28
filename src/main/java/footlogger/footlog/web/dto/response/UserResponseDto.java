package footlogger.footlog.web.dto.response;

import lombok.*;

import java.util.List;

public class UserResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResultDto {
        private Long id;
        private String nickname;
        private String profileImg;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDto {
        private Long kakaoId;
        private String nickname;
        private String profileImg;
        private String level;
        private Long stampCount;
        private List<SaveCourseDto> saveCourseList;
        private List<CheckCourseDto> checkCourseList;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveCourseDto {
        private Long courseId;
        private String name;
        private boolean isSaved;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckCourseDto {
        private Long courseId;
        private String name;
        private boolean isSaved;
    }
}
