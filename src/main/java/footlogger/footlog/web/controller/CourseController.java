package footlogger.footlog.web.controller;

import footlogger.footlog.domain.Course;
import footlogger.footlog.payload.ApiResponse;
import footlogger.footlog.service.CourseService;
import footlogger.footlog.service.S3ImageService;
import footlogger.footlog.service.SaveService;
import footlogger.footlog.web.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final SaveService saveService;
    private final S3ImageService s3ImageService;

    @Operation(summary = "지역 선택 시 해당 코스 반환")
    @GetMapping("/area/{area_name}")
    public ApiResponse<List<CourseResponseDTO>> coursesFromArea(
            @RequestHeader String token,
            @PathVariable String area_name
    ) {
        //임시 유저값
        List<CourseResponseDTO> courses = courseService.getByAreaName(token, area_name);

        return ApiResponse.onSuccess(courses);
    }

    @Operation(summary = "선호도 코스 분석")
    @PostMapping("/analyze")
    public ApiResponse<List<CourseResponseDTO>> analyzePreference(
            @RequestHeader String token,
            @RequestBody PreferenceRequestBody requestBody
            ) {
        return ApiResponse.onSuccess(courseService.analyzePreference(token, requestBody));
    }

    @Operation(summary = "추천 코스 조회")
    @GetMapping("/recommend")
    public ApiResponse<List<CourseResponseDTO>> recommendCourse(
            @RequestHeader String token
    ) {
        return ApiResponse.onSuccess(courseService.getRecommendCourse(token));
    }

    @Operation(summary = "코스 클릭 시 상세 정보 조회")
    @GetMapping("/detail/{course_id}")
    public ApiResponse<CourseDetailDTO> courseDetail(
            @RequestHeader String token,
            @PathVariable Long course_id
    ) {
        //임시 유저값
        CourseDetailDTO course = courseService.getCourseDetail(token, course_id);

        return ApiResponse.onSuccess(course);
    }

    @Operation(summary = "코스 저장 버튼 클릭 시 저장/취소 토글 기능")
    @PostMapping("/save/{course_id}")
    public ApiResponse<SaveStatusDTO> saveCourse(
            @RequestHeader String token,
            @PathVariable Long course_id
    ) {
        SaveStatusDTO dto = courseService.toggleSaveCourse(token, course_id);
        return ApiResponse.onSuccess(dto);
    }

    @Operation(summary = "코스 네이버 포스트 조회")
    @GetMapping(value = "/post/{course_id}")
    public ApiResponse<List<NaverBlogDTO>> getNaverBlogPost(
            @PathVariable(value = "course_id")Long course_id
    ) {
        return ApiResponse.onSuccess(courseService.getNaverBlogs(course_id));
    }

    @Operation(summary = "코스 완주하기")
    @PostMapping(value = "/complete/{course_id}")
    public ApiResponse<CourseIdDTO> completeCourse(
            @RequestHeader String token,
            @PathVariable(value = "course_id") Long course_id
    ) {
        return ApiResponse.onSuccess(CourseIdDTO.builder()
                        .courseId(courseService.completeCourse(token, course_id)).build());
    }

    @Operation( summary = "이미지 업로드 테스트")
    @PostMapping(value = "/s3/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> s3Upload(@RequestParam(value = "image") MultipartFile image) {
        String profileImage = s3ImageService.upload(image);
        return ResponseEntity.ok(profileImage);
    }


}
