package hufs.lion.team404.oauth.jwt;

import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import hufs.lion.team404.domain.enums.UserRole;

import javax.crypto.SecretKey;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.access-token-expire-time}")
    private long accessTokenExpireTime;
    
    @Value("${jwt.refresh-token-expire-time}")
    private long refreshTokenExpireTime;
    
    private SecretKey key;
    
    @PostConstruct
    public void init() {
        // Base64로 인코딩된 키를 디코딩하거나, 직접 키 생성
        if (secretKey.length() < 64) {
            // 키가 짧은 경우 안전한 키 생성
            this.key = Jwts.SIG.HS512.key().build();
        } else {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }
    
    // Access Token 생성
    public String createAccessToken(Long userId, String email, UserRole role) {
        System.out.println("=== JWT AccessToken 생성 시작 ===");
        System.out.println("입력값 - userId: " + userId + ", email: " + email + ", role: " + role);
        
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpireTime);
        
        String token = Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("role", role.name())
                .claim("type", "ACCESS")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
        
        System.out.println("생성된 토큰 subject(userId): " + userId.toString());
        System.out.println("=== JWT AccessToken 생성 완료 ===");
        
        return token;
    }
    
    // Refresh Token 생성
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpireTime);
        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", "REFRESH")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }
    
    // 토큰에서 사용자 정보 추출
    public Authentication getAuthentication(String token) {
        System.out.println("=== JWT 토큰 파싱 시작 ===");
        Claims claims = parseClaims(token);
        
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get("role").toString()})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        
        System.out.println("파싱된 claims = " + claims);
        System.out.println("파싱된 userId = " + claims.getSubject());
        System.out.println("파싱된 email = " + claims.get("email"));
        System.out.println("파싱된 role = " + claims.get("role"));
        System.out.println("생성된 authorities = " + authorities);
        
        UserPrincipal principal = UserPrincipal.builder()
                .id(Long.parseLong(claims.getSubject()))
                .email(claims.get("email").toString())
                .authorities(authorities)
                .build();
        
        System.out.println("생성된 UserPrincipal - ID: " + principal.getId() + ", Email: " + principal.getEmail());
        System.out.println("=== JWT 토큰 파싱 완료 ===");
        
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
    
    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
    
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    
    // 토큰에서 사용자 ID 추출
    public Long getUserId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }
}
