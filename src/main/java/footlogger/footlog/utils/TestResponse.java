package footlogger.footlog.utils;

import lombok.Data;

import java.util.List;

@Data
public class TestResponse {

    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<Item> item;
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
