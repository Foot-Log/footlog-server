package footlogger.footlog.repository.custom;

import footlogger.footlog.domain.Course;

import java.util.List;

public interface CustomSaveRepository {

    List<Course> findCoursesByUserId(Long userId);
}
