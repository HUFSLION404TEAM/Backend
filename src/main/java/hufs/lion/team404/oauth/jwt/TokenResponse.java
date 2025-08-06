package hufs.lion.team404.oauth.jwt;

import hufs.lion.team404.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
	private Long expiresIn;
	private UserInfo userInfo;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserInfo {
		private Long id;
		private String email;
		private String name;
		private UserRole role;
		private String profileImage;
		private boolean needsTypeSelection;
	}
}

