package footlogger.footlog.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseResponseData {
    private CourseResponseData.Response response;

    @Data
    public static class Response {
        private CourseResponseData.Header header;
        private CourseResponseData.Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private CourseResponseData.Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<CourseResponseData.Item> item;
    }

    @Data
    public static class Item {
        private String addr;
        private String areacode;
        private String contentid;
        private String createdtime;
        private String mainimage;
        private String cpyrhtDivCd;
        private String modifiedtime;
        private String sigungucode;
        private String subtitle;
        private String summary;
        private String tel;
        private String telname;
        private String title;
    }
}
