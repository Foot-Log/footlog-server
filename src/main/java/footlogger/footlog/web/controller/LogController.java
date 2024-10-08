package footlogger.footlog.web.controller;

import footlogger.footlog.payload.ApiResponse;
import footlogger.footlog.service.LogService;
import footlogger.footlog.web.dto.response.LogResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static footlogger.footlog.payload.code.status.ErrorStatus._INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {
    private final LogService logService;

    @Operation(summary = "완주한 코스 리스트")
    @GetMapping("/completedList")
    public ApiResponse<List<LogResponseDto.LogDto>> getCompletedList(@RequestHeader("Authorization") String token){
        String tokenWithoutBearer = token.substring(7);
        List<LogResponseDto.LogDto> response = logService.getCompletedList(tokenWithoutBearer);
        return ApiResponse.onSuccess(response);
   }
    @Operation(summary = "로그 기록보기")
   @GetMapping("/detail/{logId}")
    public ApiResponse<LogResponseDto.LogDetailDto> getLogDetail(@RequestHeader("Authorization") String token, @PathVariable("logId") Long logId){
       String tokenWithoutBearer = token.substring(7);
       LogResponseDto.LogDetailDto response = logService.getLogDetail(tokenWithoutBearer, logId);
       return ApiResponse.onSuccess(response);
   }

    @Operation(summary = "로그 기록 업데이트")
   @PatchMapping(value = "update/{logId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<LogResponseDto.LogDetailDto>> updateLog(
           @RequestHeader("Authorization") String token,
           @PathVariable("logId") Long logId,
           @RequestParam(required = false) String logContent,
           @RequestParam(required = false) List<String> existingUrls,
           @RequestPart(required = false) List<MultipartFile> newImages
           ) {
        try{
            String tokenWithoutBearer = token.substring(7);
            LogResponseDto.LogDetailDto response = logService.updateLog(tokenWithoutBearer, logId, logContent, existingUrls, newImages);
            return new ResponseEntity<>(ApiResponse.onSuccess(response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.onFailure(_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

   }
}
