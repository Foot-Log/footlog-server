package footlogger.footlog.repository;

import footlogger.footlog.domain.Log;
import footlogger.footlog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
    Log findLogById(Long id);

    void deleteByUser(User user);

}
