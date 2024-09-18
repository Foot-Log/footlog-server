package footlogger.footlog.repository;

import footlogger.footlog.domain.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {
    List<Sigungu> findByAreaCodeAndSigunguCode(Long areaCode, Long sigunguCode);
}
