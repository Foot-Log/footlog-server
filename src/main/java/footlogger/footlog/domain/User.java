package footlogger.footlog.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    private Long id;

    private String email;
    private Long kakaoId;
    private String nickname;
    private String profileImg;

    private String level;
    private Long stampCount;

    @Setter
    private String refreshToken;
}
