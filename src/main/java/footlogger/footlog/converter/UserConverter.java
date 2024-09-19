package footlogger.footlog.converter;

import footlogger.footlog.domain.CheckCourse;
import footlogger.footlog.domain.SaveCourse;
import footlogger.footlog.domain.User;
import footlogger.footlog.web.dto.response.UserResponseDto;

import java.util.stream.Collectors;

public class UserConverter {
    public static UserResponseDto.UserInfoDto toUserInfo(User user) {
        long currentStampCount = user.getStampCount() != null ? user.getStampCount() : 0;

        String levelName = getLevelName(currentStampCount);

        long stampsInCurrentLevel = (currentStampCount - 1) % 5 + 1;

        return UserResponseDto.UserInfoDto.builder()
                .kakaoId(user.getKakaoId())
                .nickname(user.getNickname())
                .level(levelName)
                .stampCount(stampsInCurrentLevel)
                .profileImg(user.getProfileImg())
                .build();
    }

    public static String getLevelName(long stampCount){
        if (stampCount <= 5) {
            return "새싹 플로거";
        } else if (stampCount <= 10) {
            return "초보 플로거";
        } else if (stampCount <= 15) {
            return "중수 플로거";
        } else if (stampCount <= 20) {
            return "고수 플로거";
        } else {
            return "베스트 플로거";
        }

    }
    public static UserResponseDto.SaveCourseDto toSaveCourse(SaveCourse saveCourse) {
        return UserResponseDto.SaveCourseDto.builder()
                .courseId(saveCourse.getCourse().getId())
                .name(saveCourse.getCourse().getName())
                .isSaved(true)
                .build();

    }

    public static UserResponseDto.CheckCourseDto toCheckCourse(CheckCourse checkCourse) {
        return UserResponseDto.CheckCourseDto.builder()
                .courseId(checkCourse.getCourse().getId())
                .name(checkCourse.getCourse().getName())
                .isSaved(checkCourse.isSaved())
                .build();
    }
}
