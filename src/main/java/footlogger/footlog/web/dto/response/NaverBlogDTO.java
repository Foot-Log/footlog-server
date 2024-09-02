package footlogger.footlog.web.dto.response;

import lombok.Builder;

@Builder
public class NaverBlogDTO {
    private String title;
    private String link;
    private String summary;
    private String blog_name;
    private String post_date;
}
