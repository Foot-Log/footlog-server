package footlogger.footlog.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseResponseDTO {
    private Long course_id;
    private String image;
    private String area;
    private String name;
    private Boolean isSave;
}
