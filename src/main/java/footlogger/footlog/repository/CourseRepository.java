package footlogger.footlog.repository;

import footlogger.footlog.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByIdIn(List<Long> ids);

    //지역 코드에 맞게 코스들을 저장수 순으로 불러옴
    @Query("SELECT c FROM Course c WHERE c.areaCode = :areaCode ORDER BY c.totalSaves")
    List<Course> findByAreaCode(@Param("areaCode") Long areaCode);
}
