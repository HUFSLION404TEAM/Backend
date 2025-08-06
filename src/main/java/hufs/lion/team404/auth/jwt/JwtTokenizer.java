package hufs.lion.team404.auth.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

// 비밀 키 생성 (Base 64)
// Access Token, Refresh Token 생성
// 토큰 유효시간 계산
// 토큰 검증 (Optional)
public class JwtTokenizer {

    @Value("${jwt.secret}")
    private String secretKey;
    private int accessTokenExpirationMinutes = 30;
    private int refreshTokenExpirationMinutes = 60 * 24 * 7;

    public String encodeBase64SecretKey(String secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String subject,
                                       Date expiration,
                                       String base64EncodedSecretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Date getTokenExpiration(int expirationMinutes) {
        return new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public int getAccessTokenExpirationMinutes() {
        return accessTokenExpirationMinutes;
    }

    public int getRefreshTokenExpirationMinutes() {
        return refreshTokenExpirationMinutes;
    }
}
