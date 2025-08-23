package hufs.lion.team404.oauth.jwt;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {
	
	@NotBlank(message = "Refresh Token은 필수입니다.")
	private String refreshToken;
}
