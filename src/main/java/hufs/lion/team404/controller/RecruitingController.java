package hufs.lion.team404.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hufs.lion.team404.domain.dto.response.ApiResponse;
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

}
