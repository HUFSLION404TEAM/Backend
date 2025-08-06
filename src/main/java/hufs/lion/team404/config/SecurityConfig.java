package hufs.lion.team404.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import hufs.lion.team404.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable()) // 개발 단계에서는 비활성화
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests
				.requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
				.requestMatchers("/api/auth/**").permitAll() // 인증 관련 API
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
				.requestMatchers("/api/**").authenticated() // API는 인증 필요
				.anyRequest().authenticated()
			)
			.logout(logout -> logout.logoutSuccessUrl("/")) //로그아웃 시 리다이렉트될 URL을 설정
			.oauth2Login(oauth2Login -> oauth2Login
				.defaultSuccessUrl("/")// OAuth 2 로그인 설정 진입점
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
					.userService(customOAuth2UserService) // OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
				)
			);

		return http.build();
	}
}
