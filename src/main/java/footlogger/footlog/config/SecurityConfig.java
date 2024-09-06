package footlogger.footlog.config;

import footlogger.footlog.jwt.CustomAccessDeniedHandler;
import footlogger.footlog.jwt.CustomJwtAuthenticationEntryPoint;
import footlogger.footlog.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomJwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler jwtAccessDeniedHandler;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**", "/swagger/**", "/swagger-resources/**", "/swagger-ui.html"
            ,"/course/**", "/log/**", "/user/**", "/v3/api-docs/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CustomJwtAuthenticationEntryPoint customJwtAuthenticationEntryPoint) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) //csrf 공격을 대비하기 위한 csrf 토큰 disable 하기
                .formLogin(AbstractHttpConfigurer::disable) //form login 비활성화 jwt를 사용하고 있으므로 폼 기반 로그인은 필요하지 않다.
                .httpBasic(AbstractHttpConfigurer::disable)//http 기본 인증은 사용자 이름과 비밀번호를 평문으로 전송하기 때문에 보안적으로 취약, 기본 인증을 비활성화 하고 있음
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .exceptionHandling(exception ->
                {
                    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                    exception.accessDeniedHandler(jwtAccessDeniedHandler);
                })
                .cors(cors -> {})
                .authorizeRequests(auth -> {
                    auth.requestMatchers(AUTH_WHITELIST).permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
