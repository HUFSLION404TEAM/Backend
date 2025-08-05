package hufs.lion.team404.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import hufs.lion.team404.service.KakaoOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final KakaoOAuth2UserService kakaoOAuth2UserService;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 개발 단계에서는 비활성화
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/health", "/h2-console/**", "/login/**", "/oauth2/**").permitAll()
                .anyRequest().authenticated()
            )

            .headers(headers -> headers.frameOptions().disable()) // H2 Console 접근을 위해

            // OAuth2 로그인은 나중에 설정
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/",true)
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(kakaoOAuth2UserService) // 네가 만든 서비스 연결
                )
             );

        return http.build();
    }
}
