package hufs.lion.team404.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
@CrossOrigin(origins = "*")
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
		Long recruiting_id = recruitingModel.createRecruiting(userId, title, recruitmentPeriod, progressPeriod, price,
			projectOutline,
			expectedResults, detailRequirement, images);

		return ApiResponse.success(recruiting_id);
	}

	@GetMapping("/")
	@Operation(
		summary = "구인글 전체 조회",
		description = "모든 구인글 목록을 조회합니다."
	)
	public ApiResponse<List<RecruitingListResponse>> getAllRecruitings() {

		List<RecruitingListResponse> responses = recruitingModel.getAllRecruitings();
		return ApiResponse.success("구인글 목록을 성공적으로 조회했습니다.", responses);
	}

	@GetMapping("/{recruitingId}")
	@Operation(
		summary = "구인글 상세 조회",
		description = "구인글의 상세 정보를 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<RecruitingDetailResponse> getRecruitingDetail(
		@PathVariable Long recruitingId,
		@AuthenticationPrincipal UserPrincipal userPrincipal) {

		RecruitingDetailResponse response = recruitingModel.getRecruitingDetail(recruitingId);
		return ApiResponse.success("구인글 상세 정보를 성공적으로 조회했습니다.", response);
	}

}
