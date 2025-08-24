package hufs.lion.team404.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hufs.lion.team404.domain.dto.request.ProjectRequestCreateRequestDto;
import hufs.lion.team404.domain.dto.request.ProjectRequestUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.entity.ProjectRequest;
import hufs.lion.team404.model.ProjectRequestModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/projectRequest")
@RequiredArgsConstructor
@Slf4j

@Tag(name = "의뢰서 제안", description = "의뢰서 제안 관련 API")
public class ProjectRequestController {

	private final ProjectRequestModel projectRequestModel;

	// 생성
	@PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(
		summary = "의뢰서 생성",
		description = "새로운 의뢰서를 생성합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> createProjectRequest(
		@AuthenticationPrincipal UserPrincipal user,
		@Valid
		@org.springdoc.core.annotations.ParameterObject
		@ModelAttribute ProjectRequestCreateRequestDto dto,
		@RequestPart(value = "files", required = false)
		@io.swagger.v3.oas.annotations.media.ArraySchema(
			arraySchema = @Schema(description = "첨부파일 리스트"),
			schema = @Schema(type = "string", format = "binary")
		)
		List<MultipartFile> files
	) {

		Long id = projectRequestModel.createProjectRequest(dto, user.getId(), files);
		return ApiResponse.success("의뢰가 성공적으로 생성되었습니다.", id);
	}

	// 조회
	@GetMapping("/{projectRequestId}")
	@Operation(
		summary = "의뢰서 조회",
		description = "의뢰서를 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<ProjectRequest> getProjectRequest(@PathVariable Long projectRequestId) {

		ProjectRequest projectRequest = projectRequestModel.getProjectRequest(projectRequestId);

		return ApiResponse.success("의뢰서가 성공적으로 조회되었습니다.", projectRequest);
	}

	// 수정
	@PutMapping(value = "/{projectRequestId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(
		summary = "의뢰서 수정",
		description = "의뢰서를 수정합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> updateProjectRequestJson(
		@PathVariable Long projectRequestId,
		@AuthenticationPrincipal UserPrincipal user,
		@Valid
		@org.springdoc.core.annotations.ParameterObject
		@ModelAttribute ProjectRequestUpdateRequestDto dto,
		@RequestPart(value = "files", required = false)
		@io.swagger.v3.oas.annotations.media.ArraySchema(
			arraySchema = @Schema(description = "첨부파일 리스트"),
			schema = @Schema(type = "string", format = "binary")
		)
		List<MultipartFile> files,
		@RequestParam(name = "clearFiles", required = false, defaultValue = "false") boolean clearFiles
	) {
		Long userId = user.getId();
		projectRequestModel.update(projectRequestId, dto, userId, files, clearFiles);
		return ApiResponse.success("의뢰서를 수정했습니다.");
	}

	// 삭제
	@DeleteMapping("/{projectRequestId}")
	@Operation(
		summary = "의뢰서 삭제",
		description = "의뢰서를 삭제합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> deleteProjectRequest(
		@PathVariable("projectRequestId") Long projectRequestId,
		@AuthenticationPrincipal UserPrincipal authentication) {

		Long id = authentication.getId();
		projectRequestModel.deleteProjectRequest(projectRequestId, id);

		return ApiResponse.success("포트폴리오가 삭제되었습니다.");
	}
}
