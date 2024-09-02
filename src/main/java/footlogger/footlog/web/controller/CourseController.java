package footlogger.footlog.web.controller;

import footlogger.footlog.domain.Course;
import footlogger.footlog.payload.ApiResponse;
import footlogger.footlog.service.CourseService;
import footlogger.footlog.service.S3ImageService;
import footlogger.footlog.service.SaveService;
import footlogger.footlog.web.dto.response.CourseDetailDTO;
import footlogger.footlog.web.dto.response.CourseResponseDTO;
import footlogger.footlog.web.dto.response.NaverBlogDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final SaveService saveService;
    private final S3ImageService s3ImageService;

    @Operation(summary = "지역 선택 시 해당 코스 반환")
    @GetMapping("/area/{area_name}")
    public ApiResponse<List<CourseResponseDTO>> coursesFromArea(
            @PathVariable String area_name
    ) {
        //임시 유저값
        Long user_id = 1L;
        List<CourseResponseDTO> courses = courseService.getByAreaName(area_name, user_id);

        return ApiResponse.onSuccess(courses);
    }

    @Operation(summary = "코스 클릭 시 상세 정보 조회")
    @GetMapping("/detail/{course_id}")
    public ApiResponse<CourseDetailDTO> courseDetail(
            @PathVariable Long course_id
    ) {
        //임시 유저값
        Long user_id = 1L;
        CourseDetailDTO course = courseService.getCourseDetail(course_id, user_id);

        return ApiResponse.onSuccess(course);
    }

    @Operation(summary = "코스 저장 버튼 클릭 시 저장/취소 토글 기능")
    @PostMapping("/save/{course_id}")
    public ApiResponse<Boolean> saveCourse(
            @PathVariable Long course_id
    ) {
        Long user_id = 1L;
        courseService.toggleSaveCourse(course_id, user_id);

        return ApiResponse.onSuccess(saveService.getSaveStatus(course_id, user_id));
    }

    @Operation(summary = "코스 네이버 포스트 조회")
    @GetMapping(value = "/post/{course_id}")
    public ApiResponse<List<NaverBlogDTO>> getNaverBlogPost(
            @RequestParam(value = "course_id")Long courseId
    ) {
        return ApiResponse.onSuccess(courseService.getNaverBlogs(courseId));
    }

    @Operation( summary = "이미지 업로드 테스트")
    @PostMapping(value = "/s3/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> s3Upload(@RequestParam(value = "image") MultipartFile image) {
        String profileImage = s3ImageService.upload(image);

        return ResponseEntity.ok(profileImage);
    }
}
