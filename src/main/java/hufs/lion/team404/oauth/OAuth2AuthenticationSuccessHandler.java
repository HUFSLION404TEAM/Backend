package hufs.lion.team404.oauth;

import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.oauth.jwt.JwtTokenProvider;
import hufs.lion.team404.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    
    // 프론트엔드 URL (환경에 따라 설정)
    @Value("${app.oauth2.authorized-redirect-uri:http://localhost:3000/oauth2/redirect}")
    private String redirectUri;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        System.out.println("oauth2User attributes = " + oauth2User.getAttributes());
        
        // registrationId를 통해 어떤 소셜 로그인인지 확인
        String registrationId = extractRegistrationId(request);
        String userNameAttributeName = oauth2User.getName();
        
        System.out.println("registrationId = " + registrationId);
        System.out.println("userNameAttributeName = " + userNameAttributeName);
        
        // OAuthAttributes를 사용해서 올바르게 사용자 정보 추출
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());
        
        String email = attributes.getEmail();
        System.out.println("추출된 이메일: " + email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        
        // TEMP 사용자인 경우와 일반 사용자인 경우 구분
        if (user.getUserRole() == UserRole.TEMP) {
            // 타입 선택이 필요한 경우
            String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("token", accessToken)
                    .queryParam("refresh", refreshToken)
                    .queryParam("needsTypeSelection", "true")
                    .queryParam("userId", user.getId())
                    .build().toUriString();
            
            response.sendRedirect(targetUrl);
        } else {
            // 완전한 사용자인 경우
            String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("token", accessToken)
                    .queryParam("refresh", refreshToken)
                    .queryParam("needsTypeSelection", "false")
                    .build().toUriString();
            
            response.sendRedirect(targetUrl);
        }
        
        log.info("OAuth2 인증 성공. 사용자: {}, 역할: {}", user.getEmail(), user.getUserRole());
    }
    
    private String extractRegistrationId(HttpServletRequest request) {
        // URL에서 registrationId 추출 (예: /oauth2/authorization/naver)
        String requestUri = request.getRequestURI();
        if (requestUri.contains("/naver")) {
            return "naver";
        } else if (requestUri.contains("/google")) {
            return "google";
        } else if (requestUri.contains("/kakao")) {
            return "kakao";
        }
        
        // 세션에서 추출하는 방법도 있음
        String referer = request.getHeader("Referer");
        if (referer != null) {
            if (referer.contains("naver")) return "naver";
            if (referer.contains("google")) return "google";
            if (referer.contains("kakao")) return "kakao";
        }
        
        return "google"; // 기본값
    }
}
