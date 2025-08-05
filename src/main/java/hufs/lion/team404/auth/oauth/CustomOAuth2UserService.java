package hufs.lion.team404.auth.oauth;

import hufs.lion.team404.auth.domain.User;
import hufs.lion.team404.auth.dto.OAuthAttributes;
import hufs.lion.team404.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 카카오 API에 실제 요청 보내서 사용자 정보 받아옴
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);

        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        OAuthAttributes attributes = OAuthAttributes.ofKakao(originAttributes);

        // DB에 해당 유저가 있는지 확인
        // 있으면 업데이트,없으면 새로 저장
        User user = saveOrUpdate(attributes);

        // Spring Security에서 로그인한 유저의 권한 목록 만듦 (ROLE_USER) 같은거
        String email = user.getEmail();
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(email);

        return new OAuth2CustomUser(registrationId, originAttributes, authorities, email);
    }

    private User saveOrUpdate(OAuthAttributes authAttributes) {
        User user = userRepository.findByEmail(authAttributes.getEmail())
                .map(entity -> entity.update(authAttributes.getName(), authAttributes.getProfileImageUrl))
                .orElse(authAttributes.toEntity());

        return userRepository.save(user);
    }
}
