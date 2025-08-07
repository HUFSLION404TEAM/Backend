package hufs.lion.team404.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hufs.lion.team404.domain.dto.response.UserResponse;
import hufs.lion.team404.model.UserModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final UserModel userModel;

	// 간단한 자기 정보 조회
	@PostMapping("/info")
	public ResponseEntity<UserResponse> info(@AuthenticationPrincipal UserPrincipal authentication) {
		Long user_id = authentication.getId();
		return ResponseEntity.ok(userModel.getMyInfo(user_id));
	}

}
