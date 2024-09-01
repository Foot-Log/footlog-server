package footlogger.footlog.web.controller;

import footlogger.footlog.domain.Log;
import footlogger.footlog.payload.ApiResponse;
import footlogger.footlog.service.LogService;
import footlogger.footlog.web.dto.response.LogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {
    private final LogService logService;

    @GetMapping("/completedList")
    public ApiResponse<LogResponseDto.LogListDto> getCompletedList(@RequestHeader("Authorization") String token){
        String tokenWithoutBearer = token.substring(7);
        LogResponseDto.LogListDto response = logService.getCompletedList(tokenWithoutBearer);
        return ApiResponse.onSuccess(response);
   }
   @GetMapping("/detail/{logId}")
    public ApiResponse<LogResponseDto.LogDetailDto> getLogDetail(@RequestHeader("Authorization") String token, @PathVariable("logId") Long logId){
       String tokenWithoutBearer = token.substring(7);
       LogResponseDto.LogDetailDto response = logService.getLogDetail(tokenWithoutBearer, logId);
       return ApiResponse.onSuccess(response);
   }
}
