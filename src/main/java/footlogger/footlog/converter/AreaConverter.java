package footlogger.footlog.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AreaConverter {
    private static final Map<Integer, String> codeToName = new HashMap<>();
    private static final Map<String, Integer> nameToCode = new HashMap<>();

    //Map 설정
    static {
        codeToName.put(1, "서울");
        codeToName.put(2, "인천");
        codeToName.put(3, "대전");
        codeToName.put(4, "대구");
        codeToName.put(5, "광주");
        codeToName.put(6, "부산");
        codeToName.put(7, "울산");
        codeToName.put(8, "세종");
        codeToName.put(31, "경기도");
        codeToName.put(32, "강원도");
        codeToName.put(33, "충청북도");
        codeToName.put(34, "충청남도");
        codeToName.put(35, "경상북도");
        codeToName.put(36, "경상남도");
        codeToName.put(37, "전라북도");
        codeToName.put(38, "전라남도");
        codeToName.put(39, "제주도");

        codeToName.forEach((code, name) -> nameToCode.put(name, code));
    }

    //코드를 지역으로 변환
    public Integer getCodeByAreaName(String name) {
        return nameToCode.getOrDefault(name, 0);
    }

    //지역을 코드로 변환
    public String getAreaNameByCode(Integer code) {
        return codeToName.getOrDefault(code, "Unknown Name");
    }
}
