package footlogger.footlog.repository;

import footlogger.footlog.domain.Log;
import footlogger.footlog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    Log findLogById(Long id);

    List<Log> findLogByUserId(Long userId);

    void deleteByUser(User user);


}
