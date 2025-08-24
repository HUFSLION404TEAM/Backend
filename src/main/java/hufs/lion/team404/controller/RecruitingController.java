package hufs.lion.team404.controller;

import java.util.List;

import hufs.lion.team404.domain.dto.response.RecruitingResponse;
import hufs.lion.team404.domain.entity.Recruiting;
import hufs.lion.team404.domain.entity.RecruitingImage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
import org.springframework.web.server.ResponseStatusException;

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


	@GetMapping("/{recruitingId}")
	@Operation(
			summary = "구인글 조회",
			description = "가게의 구인글을 조회해줍니다.",
			security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<RecruitingResponse> getRecruitingById(@PathVariable Long recruitingId) {

		Recruiting recruiting;
		try {
			recruiting = recruitingModel.getRecruitingById(recruitingId);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 구인글을 찾을 수 없습니다.");
		}

		RecruitingResponse dto = RecruitingResponse.builder()
				.id(recruiting.getId())
				.title(recruiting.getTitle())
				.imagesUrl(
						recruiting.getImages() == null ? List.of()
								: recruiting.getImages().stream()
								.map(RecruitingImage::getImagePath)
								.toList()
				)
				.recruitmentPeriod(recruiting.getRecruitmentPeriod())
				.progressPeriod(recruiting.getProgressPeriod())
				.price(recruiting.getPrice())
				.projectOutline(recruiting.getProjectOutline())
				.expectedResults(recruiting.getExpectedResults())
				.detailRequirement(recruiting.getDetailRequirement())
				.build();

		return ApiResponse.success("공고가 성공적으로 조회되었습니다.", dto);
	}


}
