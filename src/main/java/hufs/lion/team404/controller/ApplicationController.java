package hufs.lion.team404.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hufs.lion.team404.domain.dto.request.ApplicationCreateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.ApplicationResponse;
import hufs.lion.team404.model.ApplicationModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "지원서", description = "학생 지원 관련 API")
@RequestMapping("/api/application")
public class ApplicationController {
	private final ApplicationModel applicationModel;

	@PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(
		summary = "지원서 생성",
		description = "새로운 지원서를 생성합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> createApplication(
		@AuthenticationPrincipal UserPrincipal user,
		@Valid
		@org.springdoc.core.annotations.ParameterObject
		@ModelAttribute ApplicationCreateRequestDto dto,
		@RequestPart(value = "files", required = false)
		@io.swagger.v3.oas.annotations.media.ArraySchema(
			arraySchema = @Schema(description = "첨부파일 리스트"),
			schema = @Schema(type = "string", format = "binary")
		)
		List<MultipartFile> files
	) {
		Long applicationId = applicationModel.createApplication(dto, user.getId(), files);
		return ApiResponse.success("지원서가 성공적으로 생성되었습니다.", applicationId);
	}

	// 조회 - 의뢰서 API와 동일한 구조
	@GetMapping("/{applicationId}")
	@Operation(
		summary = "지원서 조회",
		description = "지원서를 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<ApplicationResponse> getApplication(@PathVariable Long applicationId) {
		ApplicationResponse application = applicationModel.getApplication(applicationId);
		return ApiResponse.success("지원서가 성공적으로 조회되었습니다.", application);
	}
}