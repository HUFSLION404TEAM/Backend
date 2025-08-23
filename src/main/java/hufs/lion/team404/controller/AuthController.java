package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.UserResponse;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.model.CustomUserModel;
import hufs.lion.team404.oauth.jwt.JwtTokenProvider;
import hufs.lion.team404.oauth.jwt.TokenRefreshRequest;
import hufs.lion.team404.oauth.jwt.TokenResponse;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import hufs.lion.team404.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "인증", description = "사용자 인증 관련 API")
public class AuthController {

	private final CustomUserModel customUserModel;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

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

	@PostMapping("/refresh")
	@Operation(
		summary = "액세스 토큰 재발급",
		description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다."
	)
	public ApiResponse<TokenResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
		try {
			// Refresh Token 유효성 검증
			if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
				throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
			}

			// Refresh Token에서 사용자 ID 추출
			Long userId = jwtTokenProvider.getUserId(request.getRefreshToken());

			// 사용자 정보 조회
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

			// 새로운 토큰 생성
			String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(),
				user.getUserRole());
			String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());

			// 사용자 정보 설정
			TokenResponse.UserInfo userInfo = TokenResponse.UserInfo.builder()
				.id(user.getId())
				.email(user.getEmail())
				.name(user.getName())
				.role(user.getUserRole())
				.profileImage(user.getProfileImage())
				.needsTypeSelection(user.getStudent() == null && user.getStores().isEmpty())
				.build();

			// 토큰 응답 생성
			TokenResponse tokenResponse = TokenResponse.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken)
				.tokenType("Bearer")
				.expiresIn(3600L) // 1시간 (초 단위)
				.userInfo(userInfo)
				.build();

			return ApiResponse.success("토큰이 성공적으로 재발급되었습니다.", tokenResponse);

		} catch (Exception e) {
			log.error("토큰 재발급 실패: {}", e.getMessage());
			if (e instanceof CustomException) {
				throw e;
			}
			throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
		}
	}
}
