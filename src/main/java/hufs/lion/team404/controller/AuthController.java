package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.UserResponse;
import hufs.lion.team404.model.CustomUserModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "인증", description = "사용자 인증 관련 API")
public class AuthController {

	private final CustomUserModel customUserModel;

	@PostMapping("/info")
	@Operation(
		summary = "내 정보 조회", 
		description = "현재 로그인한 사용자의 정보를 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<UserResponse> info(@AuthenticationPrincipal UserPrincipal authentication) {
		Long userId = authentication.getId();
		UserResponse userResponse = customUserModel.getMyInfo(userId);
		return ApiResponse.success("사용자 정보를 성공적으로 조회했습니다.", userResponse);
	}
}
