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
    @Query("SELECT c FROM Course c WHERE c.areaCode = :areaCode ORDER BY c.name")
    List<Course> findByAreaCode(@Param("areaCode") Long areaCode);

    @Query("SELECT c FROM Course c ORDER BY c.name LIMIT :total")
    List<Course> findAllOrderByTotalSaves(@Param("total") Integer total);

    @Query("SELECT c FROM Course c ORDER BY c.image DESC LIMIT :total")
    List<Course> findCourseOrderByImage(@Param("total") Integer total);

    //검색어와 이름이 매칭되는 코스 불러오기
    @Query("SELECT c FROM Course c WHERE c.name LIKE %:keyword% ORDER BY c.name DESC LIMIT :limit")
    List<Course> findByNameKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);

    //검색어와 주소가 매칭되는 코스 불러오기
    @Query("SELECT c FROM Course c WHERE c.address LIKE %:keyword% ORDER BY c.name DESC LIMIT :limit")
    List<Course> findByAddressKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);

    //검색어와 내용가 매칭되는 코스 불러오기
    @Query("SELECT c FROM Course c WHERE c.content LIKE %:keyword% ORDER BY c.name DESC LIMIT :limit")
    List<Course> findByContentKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);
}
