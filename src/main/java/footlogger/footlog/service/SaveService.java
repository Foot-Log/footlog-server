package footlogger.footlog.service;

import footlogger.footlog.domain.Course;
import footlogger.footlog.repository.CourseRepository;
import footlogger.footlog.repository.SaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveService {
    private final CourseRepository courseRepository;
    private final SaveRepository saveRepository;

    //코스의 저장여부 판단
    public Boolean getSaveStatus(Long courseId, Long userId) {
        Course targetCourse = courseRepository.findById(courseId).orElse(null);
        return saveRepository.findCoursesByUserId(userId).contains(targetCourse);
    }
}
