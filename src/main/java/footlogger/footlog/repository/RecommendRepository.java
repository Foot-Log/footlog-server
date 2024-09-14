package footlogger.footlog.repository;


import footlogger.footlog.domain.RecommendCourse;
import footlogger.footlog.domain.SaveCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendRepository extends JpaRepository<RecommendCourse, Long> {
    List<RecommendCourse> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
