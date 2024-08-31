package footlogger.footlog.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseDetailDTO {
    private Long course_id;
    private String name;
    private List<String> image;
    private String summary;
    private String address;
    private String tel;
    private Boolean isSave;
}
