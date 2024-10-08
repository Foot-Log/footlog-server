package footlogger.footlog.web.controller;

import footlogger.footlog.payload.ApiResponse;
import footlogger.footlog.payload.code.status.ErrorStatus;
import footlogger.footlog.repository.CourseRepository;
import footlogger.footlog.service.AreaService;
import footlogger.footlog.service.CourseService;
import footlogger.footlog.service.SearchService;
import footlogger.footlog.web.dto.response.CourseCountDTO;
import footlogger.footlog.web.dto.response.CourseResponseDTO;
import footlogger.footlog.web.dto.response.SearchLogDTO;
import footlogger.footlog.web.dto.response.SigunguCodeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final AreaService areaService;

    @Operation(summary = "최근 검색어 조회")
    @GetMapping("/recent")
    public ApiResponse<List<SearchLogDTO>> findRecentSearchLog(
            @RequestHeader("Authorization") String token
    ) {
        String tokenWithoutBearer = token.substring(7);
        List<SearchLogDTO> recentSearchLogList = searchService.getRecentSearchLogs(tokenWithoutBearer);

        return ApiResponse.onSuccess(recentSearchLogList);
    }

    @Operation(summary = "검색어 삭제")
    @PatchMapping("/delete/{keyword}")
    public ApiResponse<CourseCountDTO> deleteSearchLog(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "keyword") String keyword
    ) {
        String tokenWithoutBearer = token.substring(7);

        try {
            String decodedKeyword = URLDecoder.decode(keyword, "UTF-8");

            return ApiResponse.onSuccess(CourseCountDTO.builder()
                    .Count(searchService.deleteRecentSearchLog(tokenWithoutBearer, decodedKeyword))
                    .build());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST, CourseCountDTO.builder().build());
        }
    }

    @Operation(summary = "코스 검색")
    @GetMapping("/course/{keyword}")
    public ApiResponse<List<CourseResponseDTO>> searchCourse(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "keyword") String keyword
    ) {
        String tokenWithoutBearer = token.substring(7);

        try {
            String decodedKeyword = URLDecoder.decode(keyword, "UTF-8");

            return ApiResponse.onSuccess(courseService.getCourseByKeyword(tokenWithoutBearer, decodedKeyword));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST, Arrays.asList(CourseResponseDTO.builder().build()));
        }


    }

    @Operation(summary = "시군구 정보 전체 조회")
    @GetMapping(value = "sigungu_code")
    public ApiResponse<List<SigunguCodeDTO>> getSigunguInfo(
            @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.onSuccess(areaService.getSigunguInfo());
    }
}
