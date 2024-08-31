package footlogger.footlog.service;

import footlogger.footlog.converter.CourseConverter;
import footlogger.footlog.domain.Course;
import footlogger.footlog.domain.User;
import footlogger.footlog.repository.CourseRepository;
import footlogger.footlog.repository.SaveRepository;
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

    //지역기반으로 코스를 받아 옴
    public List<CourseResponseDTO> getByAreaCode(int areaCode, Long userId) {
        List<Course> courses = courseRepository.findByAreaCode(areaCode);

        return courses.stream()
                .map(course -> courseConverter.toResponseDTO(course, userId))
                .collect(Collectors.toList());
    }
}
