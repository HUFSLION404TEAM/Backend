package hufs.lion.team404.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import hufs.lion.team404.domain.dto.UserDTO;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.oauth.OAuthAttributes;
import hufs.lion.team404.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final HttpSession httpSession;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		// 현재 로그인 진행 중인 서비스를 구분하는 코드 (네이버 로그인인지 구글 로그인인지 구분)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// OAuth2 로그인 진행 시 키가 되는 필드 값 (Primary Key와 같은 의미)을 의미
		// 구글의 기본 코드는 "sub", 후에 네이버 로그인과 구글 로그인을 동시 지원할 때 사용
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
			.getUserInfoEndpoint().getUserNameAttributeName();

		// OAuth2UserService를 통해 가져온 OAuthUser의 attribute를 담을 클래스 ( 네이버 등 다른 소셜 로그인도 이 클래스 사용)
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
			oAuth2User.getAttributes());

		User userEntity = saveOrUpdate(attributes);

		// UserEntity 클래스를 사용하지 않고 SessionUser클래스를 사용하는 이유는 오류 방지.
		httpSession.setAttribute("user", new UserDTO(userEntity)); // UserDTO : 세션에 사용자 정보를 저장하기 위한 Dto 클래스

		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(userEntity.getRoleKey())),
			attributes.getAttributes(),
			attributes.getNameAttributeKey());
	}

	// 구글 사용자 정보 업데이트 시 UserEntity 엔티티에 반영
	private User saveOrUpdate(OAuthAttributes attributes) {

		// 이메일을 기준으로 사용자를 찾아 업데이트하거나, 사용자를 새로 생성합니다.
		User userEntity = userRepository.findByEmail(attributes.getEmail())
			.map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
			.orElse(attributes.toEntity());

		return userRepository.save(userEntity);
	}
}