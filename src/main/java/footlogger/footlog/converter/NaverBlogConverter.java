package footlogger.footlog.converter;

import footlogger.footlog.web.dto.response.NaverBlogDTO;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class NaverBlogConverter {

    //네이버 api에서 받은 JSON값을 DTO로 변경
    public NaverBlogDTO JSONToDTO(JSONObject jsonObject) {
        return NaverBlogDTO.builder()
                .title(removeSpecificHtmlTags(jsonObject.getString("title")))
                .link(jsonObject.getString("link"))
                .summary(jsonObject.getString("description"))
                .blog_name(jsonObject.getString("bloggername"))
                .post_date(jsonObject.getString("postdate"))
                .build();
    }


    public String removeSpecificHtmlTags(String input) {
        if (input == null) {
            return null;
        }
        // <b>와 </b> 태그만 제거
        return input.replaceAll("</?b>", "");
    }

}
