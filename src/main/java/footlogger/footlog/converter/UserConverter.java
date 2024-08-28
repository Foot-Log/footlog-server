package footlogger.footlog.converter;

import footlogger.footlog.domain.CheckCourse;
import footlogger.footlog.domain.SaveCourse;
import footlogger.footlog.domain.User;
import footlogger.footlog.web.dto.response.UserResponseDto;

import java.util.stream.Collectors;

public class UserConverter {
    public static UserResponseDto.UserInfoDto toUserInfo(User user) {
        return UserResponseDto.UserInfoDto.builder()
                .kakaoId(user.getKakaoId())
                .nickname(user.getNickname())
                .level(user.getLevel())
                .stampCount(user.getStampCount())
                .profileImg(user.getProfileImg())
                .saveCourseList(user.getSaveCourseList().stream()
                        .map(UserConverter::toSaveCourse).collect(Collectors.toList()))
                .checkCourseList(user.getCheckCourseList().stream()
                        .map(UserConverter::toCheckCourse).collect(Collectors.toList()))
                .build();

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
