package hufs.lion.team404.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2UserService extends DefaultOAuth2UserService {

	private final UserService userService;  // 기존 UserService 재사용

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// 1. 카카오 사용자 정보 꺼내기
		Map<String, Object> attributes = oAuth2User.getAttributes();
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

		String email = (String) kakaoAccount.get("email");
		String nickname = (String) profile.get("nickname");
		String socialId = String.valueOf(attributes.get("id"));  // ← 카카오 고유 ID
		String socialProvider = "kakao";

		// 2. 사용자 DB 조회 or 생성 (UserService 사용)
		System.out.println("카카오 로그인 시도:");
		System.out.println("email = " + email);
		System.out.println("nickname = " + nickname);
		System.out.println("socialId = " + socialId);
		// 예: userService.findOrCreateUserByEmail(email, nickname);
		userService.findOrCreateKakaoUser(email, socialId, nickname);

		// 3. 기본 OAuth2User 리턴 (나중에 CustomOAuth2User로 확장 가능)
		return oAuth2User;
	}
}
