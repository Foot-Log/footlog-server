package footlogger.footlog.repository;

import footlogger.footlog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByEmail(String Email);
    Optional<User> findByAccessToken(String accessToken);
}
