package footlogger.footlog.web.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.color.ICC_ColorSpace;

@Getter
@NoArgsConstructor //역직렬화를 위한 기본 생성자
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDto {
    //회원 번호
    @JsonProperty("id")
    public Long id;
    //카카오 계정 정보
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        //카카오계정 이름
        @JsonProperty("name")
        public String name;
        //카카오계정 대표 이메일
        @JsonProperty("email")
        public String email;
        //사용자 프로필 정보
        @JsonProperty("profile")
        public Profile profile;

    }
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        //닉네임
        @JsonProperty("nickname")
        public String nickName;
        //프로필 미리보기 이미지 URL
        @JsonProperty("thumbnail_image_url")
        public String thumbnailImageUrl;

        //프로필 사진 URL
        @JsonProperty("profile_image_url")
        public String profileImageUrl;
    }
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Partner {
        //고유 ID
        @JsonProperty("uuid")
        public String uuid;
    }
}
