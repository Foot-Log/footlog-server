package footlogger.footlog.converter;

import footlogger.footlog.domain.Course;
import footlogger.footlog.domain.CourseImage;
import footlogger.footlog.service.CourseService;
import footlogger.footlog.service.SaveService;
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

    public CourseResponseDTO toResponseDTO(Course course, Long userId) {
        boolean isSave = saveService.getSaveStatus(course.getId(), userId);
        String area = areaConverter.getAreaNameByCode(course.getAreaCode());
        List<String> images = course.getImages().stream()
                .map(CourseImage::getImage)
                .toList();

        return CourseResponseDTO.builder()
                .course_id(course.getId())
                .image(images)
                .area(area)
                .name(course.getName())
                .isSave(isSave)
                .build();
    }
}
