package hufs.lion.team404.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.MatchingResponse;
import hufs.lion.team404.model.MatchingModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
@Slf4j

@Tag(name = "매칭", description = "매칭 수락/거절 관련 API")
public class MatchingController {

	private final MatchingModel matchingModel;

	@PostMapping("/{matchingId}/accept")
	@Operation(
		summary = "매칭 수락",
		description = "매칭을 수락합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<Long> acceptMatching(
		@PathVariable Long matchingId,
		@AuthenticationPrincipal UserPrincipal userPrincipal) {

		Long id = matchingModel.acceptMatching(matchingId, userPrincipal.getId());
		return ApiResponse.success("매칭이 수락되었습니다.", id);
	}

	@PostMapping("/{matchingId}/reject")
	@Operation(
		summary = "매칭 거절",
		description = "매칭을 거절합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<Long> rejectMatching(
		@PathVariable Long matchingId,
		@AuthenticationPrincipal UserPrincipal userPrincipal) {

		Long id = matchingModel.rejectMatching(matchingId, userPrincipal.getEmail());
		return ApiResponse.success("매칭이 거절되었습니다.", id);
	}

	@GetMapping("/student")
	@Operation(
		summary = "학생 매칭 목록 조회",
		description = "학생이 받은 매칭 목록을 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<List<MatchingResponse>> getStudentMatchings(
		@AuthenticationPrincipal UserPrincipal userPrincipal) {

		List<MatchingResponse> matchings = matchingModel.getStudentMatchings(userPrincipal.getId());
		return ApiResponse.success("매칭 목록을 성공적으로 조회했습니다.", matchings);
	}

	@GetMapping("/store")
	@Operation(
		summary = "가게 매칭 목록 조회",
		description = "학생이 받은 매칭 목록을 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<List<MatchingResponse>> getStoreMatchings(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestParam String businessNumber) {

		List<MatchingResponse> matchings = matchingModel.getStoreMatchings(userPrincipal.getId(), businessNumber);
		return ApiResponse.success("매칭 목록을 성공적으로 조회했습니다.", matchings);
	}

	@PostMapping("/{matchingId}/complete")
	@Operation(
		summary = "매칭 완료 (학생용)",
		description = "학생이 작업을 완료하고 매칭을 종료합니다. 자동으로 결제가 진행됩니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<Long> completeMatching(
		@PathVariable Long matchingId,
		@AuthenticationPrincipal UserPrincipal userPrincipal) {

		Long id = matchingModel.completeMatching(matchingId, userPrincipal.getEmail());
		return ApiResponse.success("매칭이 완료되었습니다. 결제가 진행됩니다.", id);
	}
}
