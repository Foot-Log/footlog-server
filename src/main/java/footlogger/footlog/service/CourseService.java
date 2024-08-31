package footlogger.footlog.service;

import footlogger.footlog.converter.AreaConverter;
import footlogger.footlog.converter.CourseConverter;
import footlogger.footlog.domain.Course;
import footlogger.footlog.domain.User;
import footlogger.footlog.repository.CourseRepository;
import footlogger.footlog.repository.SaveRepository;
import footlogger.footlog.web.dto.response.CourseDetailDTO;
import footlogger.footlog.web.dto.response.CourseResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;
    private final AreaConverter areaConverter;

    //지역기반으로 코스를 받아 옴
    public List<CourseResponseDTO> getByAreaName(String areaName, Long userId) {
        int areaCode = areaConverter.getCodeByAreaName(areaName);
        List<Course> courses = courseRepository.findByAreaCode(areaCode);

        return courses.stream()
                .map(course -> courseConverter.toResponseDTO(course, userId))
                .collect(Collectors.toList());
    }

    //클릭 시 상세 조회
    public CourseDetailDTO getCourseDetail(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId).orElse(null);

        return courseConverter.toDetailDTO(course, userId);
    }
}
