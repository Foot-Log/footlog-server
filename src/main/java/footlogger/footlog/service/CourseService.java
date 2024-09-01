package footlogger.footlog.service;

import footlogger.footlog.converter.AreaConverter;
import footlogger.footlog.converter.CourseConverter;
import footlogger.footlog.domain.Course;
import footlogger.footlog.domain.SaveCourse;
import footlogger.footlog.domain.User;
import footlogger.footlog.repository.CourseRepository;
import footlogger.footlog.repository.SaveRepository;
import footlogger.footlog.repository.UserRepository;
import footlogger.footlog.web.dto.response.CourseDetailDTO;
import footlogger.footlog.web.dto.response.CourseResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;
    private final AreaConverter areaConverter;
    private final SaveRepository saveRepository;
    private final UserRepository userRepository;

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

    //클릭 때 마다 코스를 저장하고 취소하는 토글 기능
    public void toggleSaveCourse(Long courseId, Long userId) {
        Optional<SaveCourse> saveCourse = saveRepository.findByCourseIdAndUserId(courseId, userId);

        //존재할 경우 테이블에서 삭제
        if (saveCourse.isPresent()) {
            saveRepository.delete(saveCourse.get());
        } else { //존재하지 않을 경우 추가
            Course course = courseRepository.findById(courseId).orElse(null);
            User user = userRepository.findById(userId).orElse(null);

            SaveCourse addSaveCourse = new SaveCourse(null, course, user);
            saveRepository.save(addSaveCourse);
        }
    }
}
