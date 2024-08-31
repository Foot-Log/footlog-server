package footlogger.footlog.repository;

import footlogger.footlog.domain.Course;
import footlogger.footlog.domain.SaveCourse;
import footlogger.footlog.repository.custom.CustomSaveRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaveRepository extends JpaRepository<SaveCourse, Long>, CustomSaveRepository {
}
