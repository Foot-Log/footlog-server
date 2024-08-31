package footlogger.footlog.repository.custom;

import footlogger.footlog.domain.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomSaveRepositoryImpl implements CustomSaveRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Course> findCoursesByUserId(Long userId) {
        return em.createQuery("SELECT sc.course FROM SaveCourse sc WHERE sc.user.id = :userId", Course.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
