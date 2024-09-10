package footlogger.footlog.web.controller;

import footlogger.footlog.payload.ApiResponse;
import footlogger.footlog.repository.CourseRepository;
import footlogger.footlog.service.CourseService;
import footlogger.footlog.service.SearchService;
import footlogger.footlog.web.dto.response.CourseResponseDTO;
import footlogger.footlog.web.dto.response.SearchLogDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @Operation(summary = "최근 검색어 조회")
    @GetMapping("/search/recent")
    public ApiResponse<List<SearchLogDTO>> findRecentSearchLog(
            @RequestHeader("Authorization") String token
    ) {
        List<SearchLogDTO> recentSearchLogList = searchService.getRecentSearchLogs(token);

        return ApiResponse.onSuccess(recentSearchLogList);
    }

    @Operation(summary = "검색어 삭제")
    @PatchMapping("/search/delete")
    public ApiResponse<Long> deleteSearchLog(
            @RequestHeader("Authorization") String token,
            @RequestBody SearchLogDTO requestBody
    ) {
        return ApiResponse.onSuccess(searchService.deleteRecentSearchLog(token, requestBody));
    }

    @Operation(summary = "검색")
    @GetMapping("/search/course/{keyword}")
    public ApiResponse<List<CourseResponseDTO>> searchCourse(
            @RequestHeader("Authorization") String token,
            @PathVariable("keyword") String keyword
    ) {
        searchService.saveRecentSearchLog(token, keyword);

        return ApiResponse.onSuccess(courseService.getByAreaName("서울", 1L));
    }
}
