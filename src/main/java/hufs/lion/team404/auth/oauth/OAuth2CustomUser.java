package hufs.lion.team404.auth.oauth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// 카카오 로그인 성공한 후 로그인 사용자 정보를 담는 객체
@Getter
public class OAuth2CustomUser implements OAuth2User, Serializable {

    private String registrationId;
    private Map<String, Object> attributes;
    private List<GrantedAuthority> authorities;
    private String email;

    public OAuth2CustomUser(String registrationId, Map<String, Object> attributes, List<GrantedAuthority> authorities, String email) {
        this.registrationId = "kakao";
        this.attributes = attributes;
        this.authorities = authorities;
        this.email = email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return this.email;
    }

    public OAuth2CustomUser(Map<String, Object> attributes, List<GrantedAuthority> authorities, String email) {
        this.attributes = attributes;
        this.authorities = authorities;
        this.email = email;
    }
}
