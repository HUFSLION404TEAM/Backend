package hufs.lion.team404.oauth;

import java.util.Map;

import hufs.lion.team404.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String picture;
	private String provider;
	private String providerId;

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
		String picture, String provider, String providerId) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.provider = provider;
		this.providerId = providerId;
	}

	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		System.out.println("sda");
		if ("naver".equals(registrationId)) {
			return ofNaver(registrationId, userNameAttributeName, attributes);
		} else if ("kakao".equals(registrationId)) {
			return ofKakao(registrationId, userNameAttributeName, attributes);
		}

		return ofGoogle(registrationId, userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.name((String)attributes.get("name"))
			.email((String)attributes.get("email"))
			.picture((String)attributes.get("picture"))
			.attributes(attributes)
			.provider(registrationId)
			.providerId((String)attributes.get("sub"))
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>)attributes.get("response");

		return OAuthAttributes.builder()
			.name((String)response.get("name"))
			.email((String)response.get("email"))
			.picture((String)response.get("profile_image"))
			.attributes(attributes)
			.provider(registrationId)
			.providerId((String)response.get("id"))
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		System.out.println("attributes = " + attributes);
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		return OAuthAttributes.builder()
			.name((String)profile.get("nickname"))
			.email((String)kakaoAccount.get("email"))
			.picture((String)profile.get("profile_image_url"))
			.attributes(attributes)
			.provider(registrationId)
			.providerId(String.valueOf(attributes.get("id")))
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	public User toEntity() {
		return User.builder()
			.name(name)
			.email(email)
			.picture(picture)
			.userRole(UserRole.STUDENT)
			.provider(provider)
			.providerId(providerId)
			.build();
	}
}