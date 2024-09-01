package footlogger.footlog.repository;

import footlogger.footlog.domain.Course;
import footlogger.footlog.domain.SaveCourse;
import footlogger.footlog.repository.custom.CustomSaveRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SaveRepository extends JpaRepository<SaveCourse, Long>, CustomSaveRepository {

    @Query("SELECT sc FROM SaveCourse sc WHERE sc.course.id = :courseId AND sc.user.id = :userId")
    Optional<SaveCourse> findByCourseIdAndUserId(@Param("courseId") Long courseId,@Param("userId") Long userId);
}
