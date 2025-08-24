package hufs.lion.team404.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import hufs.lion.team404.oauth.OAuth2AuthenticationSuccessHandler;
import hufs.lion.team404.oauth.jwt.JwtAuthenticationFilter;
import hufs.lion.team404.oauth.jwt.JwtTokenProvider;
import hufs.lion.team404.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
			.csrf(csrf -> csrf.disable()) // 개발 단계에서는 비활성화
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests
				// 공개 엔드포인트 - 인증 불필요
				.requestMatchers("/", "/health").permitAll()
				.requestMatchers("/oauth2/**", "/login/**").permitAll()
				.requestMatchers("/api/auth/**").permitAll() // 인증 관련 API
				
				// Swagger 관련
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
				.requestMatchers("/swagger-resources/**", "/webjars/**").permitAll()
				
				// H2 콘솔 (개발환경)
				.requestMatchers("/h2-console/**").permitAll()
				
				// API 엔드포인트 - 개발 단계에서는 일시적으로 허용
				.requestMatchers("/api/**").permitAll()
				
				// 나머지는 permitAll로 변경 (개발 단계)
				.anyRequest().permitAll()
			)
			// 커스텀 예외 처리 핸들러 설정
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 실패 시
				.accessDeniedHandler(customAccessDeniedHandler) // 권한 부족 시
			)
			.logout(logout -> logout.logoutSuccessUrl("/")) //로그아웃 시 리다이렉트될 URL을 설정
			.oauth2Login(oauth2Login -> oauth2Login
				.defaultSuccessUrl("/")// OAuth 2 로그인 설정 진입점
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
					.userService(customOAuth2UserService) // OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
				)
			)
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 도메인 허용 (개발 단계)
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
