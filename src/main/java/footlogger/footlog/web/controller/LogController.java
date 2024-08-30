package footlogger.footlog.web.controller;

import footlogger.footlog.service.LogService;
import footlogger.footlog.web.dto.response.LogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
