package hufs.lion.team404.oauth.jwt;

import lombok.Data;

@Data
public class TokenRefreshRequest {
	private String refreshToken;
}
