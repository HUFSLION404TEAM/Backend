package hufs.lion.team404.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.RecruitingDetailResponse;
import hufs.lion.team404.domain.dto.response.RecruitingListResponse;
import hufs.lion.team404.model.RecruitingModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
@Slf4j

@Tag(name = "구인", description = "구인 관련 API")
public class RecruitingController {
	private final RecruitingModel recruitingModel;

	@PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(
		summary = "구인글 생성",
		description = "가게의 구인글을 생성해줍니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> createRecruiting(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestParam(value = "businessNumber") String businessNumber,
		@RequestParam(value = "title") String title,
		@RequestParam(value = "recruitmentPeriod") String recruitmentPeriod,
		@RequestParam(value = "progressPeriod") String progressPeriod,
		@RequestParam(value = "price") String price,
		@RequestParam(value = "projectOutline") String projectOutline,
		@RequestParam(value = "expectedResults") String expectedResults,
		@RequestParam(value = "detailRequirement") String detailRequirement,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	) {
		Long userId = userPrincipal.getId();
		Long recruiting_id = recruitingModel.createRecruiting(userId, businessNumber, title, recruitmentPeriod, progressPeriod, price,
			projectOutline,
			expectedResults, detailRequirement, images);

		return ApiResponse.success(recruiting_id);
	}


	@GetMapping("/")
	@Operation(
		summary = "구인글 목록 조회",
		description = "모든 구인글 목록을 조회합니다. 카테고리와 제목 검색이 가능합니다."
	)
	public ApiResponse<List<RecruitingListResponse>> getAllRecruitings(
		@RequestParam(value = "category", required = false) String category,
		@RequestParam(value = "keyword", required = false) String keyword,
		@RequestParam(value = "isRecruiting", required = false) Boolean isRecruiting
	) {
		List<RecruitingListResponse> recruitings = recruitingModel.getAllRecruitings(category, keyword, isRecruiting);
		return ApiResponse.success("구인글 목록을 성공적으로 조회했습니다.", recruitings);
	}

	@GetMapping("/{recruitingId}")
	@Operation(
		summary = "구인글 상세 조회",
		description = "특정 구인글의 상세 정보를 조회합니다."
	)
	public ApiResponse<RecruitingDetailResponse> getRecruitingById(@PathVariable Long recruitingId) {
		RecruitingDetailResponse recruiting = recruitingModel.getRecruitingDetail(recruitingId);
		return ApiResponse.success("구인글을 성공적으로 조회했습니다.", recruiting);
	}
	@GetMapping("/store/{businessNumber}")
	@Operation(
		summary = "업체별 구인글 조회",
		description = "특정 업체의 구인글 목록을 조회합니다."
	)
	public ApiResponse<List<RecruitingListResponse>> getRecruitingsByStore(@PathVariable String businessNumber) {
		List<RecruitingListResponse> recruitings = recruitingModel.getRecruitingsByStore(businessNumber);
		return ApiResponse.success("업체별 구인글 목록을 성공적으로 조회했습니다.", recruitings);
	}

	@GetMapping("/my")
	@Operation(
		summary = "내 구인글 조회",
		description = "현재 로그인한 사용자의 모든 업체 구인글을 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<List<RecruitingListResponse>> getMyRecruitings(
		@AuthenticationPrincipal UserPrincipal userPrincipal
	) {
		Long userId = userPrincipal.getId();
		List<RecruitingListResponse> recruitings = recruitingModel.getMyRecruitings(userId);
		return ApiResponse.success("내 구인글 목록을 성공적으로 조회했습니다.", recruitings);
	}

	@DeleteMapping("/{recruitingId}")
	@Operation(
		summary = "구인글 삭제",
		description = "구인글을 삭제합니다. 본인의 구인글만 삭제 가능합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<Void> deleteRecruiting(
		@PathVariable Long recruitingId,
		@AuthenticationPrincipal UserPrincipal userPrincipal
	) {
		Long userId = userPrincipal.getId();
		recruitingModel.deleteRecruiting(recruitingId, userId);
		return ApiResponse.success("구인글이 성공적으로 삭제되었습니다.");
	}


}
