package footlogger.footlog.utils;

import lombok.Data;

import java.util.List;

@Data
public class SigunguResponse {
        private Response response;

    @Data
        public static class Response {
            private Header header;
            private Body body;

            // getter, setter
        }
    @Data
        public static class Header {
            private String resultCode;
            private String resultMsg;

            // getter, setter
        }
    @Data
        public static class Body {
            private Items items;
            private int numOfRows;
            private int pageNo;
            private int totalCount;
            // getter, setter
        }
    @Data
        public static class Items {
            private List<Item> item;

            // getter, setter
        }
    @Data
        public static class Item {
            private String code; // 'code' 필드
            private String name; // 'name' 필드
            private String rnum;
            // getter, setter
        }

        // getter, setter
    }

