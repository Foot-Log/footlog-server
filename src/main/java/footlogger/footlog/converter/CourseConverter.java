package footlogger.footlog.converter;

import footlogger.footlog.domain.Course;
import footlogger.footlog.domain.CourseImage;
import footlogger.footlog.service.CourseService;
import footlogger.footlog.service.SaveService;
import footlogger.footlog.web.dto.response.CourseDetailDTO;
import footlogger.footlog.web.dto.response.CourseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseConverter {
    private final SaveService saveService;
    private final AreaConverter areaConverter;

    public CourseResponseDTO toResponseDTO(Course course, Boolean isSave) {
        String area = areaConverter.getAreaNameByCode(course.getAreaCode());

        return CourseResponseDTO.builder()
                .course_id(course.getId())
                .image(course.getImage())
                .area(course.getAddress())
                .name(course.getName())
                .isSave(isSave)
                .build();
    }

    public CourseDetailDTO toDetailDTO(Course course, Boolean isSave, Boolean isComplete) {

        return CourseDetailDTO.builder()
                .course_id(course.getId())
                .name(course.getName())
                .image(course.getImage())
                .summary(course.getContent())
                .address(course.getAddress())
                .isSave(isSave)
                .isComplete(isComplete)
                .build();
    }
}
