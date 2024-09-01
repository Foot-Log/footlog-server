package footlogger.footlog.repository;

import footlogger.footlog.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
    Log findLogById(Long id);

}
