package footlogger.footlog.web.controller;

import footlogger.footlog.domain.Log;
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
    public LogResponseDto.LogListDto getCompletedList(@RequestHeader("Authorization") String token){
        String tokenWithoutBearer = token.substring(7);
        return logService.getCompletedList(tokenWithoutBearer);
   }
   @GetMapping("/detail/{logId}")
    public LogResponseDto.LogDetailDto getLogDetail(@RequestHeader("Authorization") String token, @PathVariable("logId") Long logId){
       String tokenWithoutBearer = token.substring(7);
       return logService.getLogDetail(tokenWithoutBearer, logId);
   }
}
