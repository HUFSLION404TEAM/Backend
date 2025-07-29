package hufs.lion.team404.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 개발 단계에서는 비활성화
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/health", "/h2-console/**").permitAll()
                .anyRequest().permitAll() // 일단 모든 요청 허용
            )
            .headers(headers -> headers.frameOptions().disable()); // H2 Console 접근을 위해
            // OAuth2 로그인은 나중에 설정
            // .oauth2Login(oauth2 -> oauth2
            //     .defaultSuccessUrl("/")
            // );
        
        return http.build();
    }
}
