package hufs.lion.team404.auth.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthorityUtils {

    // 이메일로부터 권한리스트 만듦 (대학생/소상공인)
    public List<String> createRoles(String email) {
        if (email.endsWith(".ac.kr")) {
            return List.of("ROLE_USER", "ROLE_STUDENT");
        } else {
            return List.of("ROLE_USER", "ROLE_OWNER");
        }

    }

    // 문자열 권한 -> 객체 변환
    public List<GrantedAuthority> createAuthorities(String email) {
        return createRoles(email).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
