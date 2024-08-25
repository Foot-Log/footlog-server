package footlogger.footlog.service;

import footlogger.footlog.domain.User;
import footlogger.footlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByProviderId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }

    public void save(User user){
        userRepository.save(user);
    }
}
