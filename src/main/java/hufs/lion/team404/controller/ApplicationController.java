package hufs.lion.team404.controller;
import hufs.lion.team404.domain.dto.request.ApplicationSaveRequestDto;
import hufs.lion.team404.service.ApplicationService;
import hufs.lion.team404.domain.dto.request.ApplicationStartRequestDto;
import hufs.lion.team404.domain.dto.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@Tag(name = "지원서", description = "사용자 지원 관련 API")
@RequestMapping("/matching/application")
public class ApplicationController {
	private final ApplicationService applicationService;
	@Operation(summary = "지원서 생성", description = "특정 가게에 대한 지원서를 DRAFT 상태로 생성합니다.")
	@PostMapping("/start")
	public ApplicationResponse start(@RequestParam Long studentId,
		@Valid @RequestBody ApplicationStartRequestDto req) {
		return applicationService.start(studentId, req);
	}
	@Operation(summary = "지원서 저장(수정)", description = "상태의 지원서를 부분 수정합니다.")
	@PutMapping("/{applicationId}")
	public ResponseEntity<ApplicationResponse> save(
		@RequestParam Long studentId,
		@PathVariable Long applicationId,
		@RequestBody ApplicationSaveRequestDto req
	) {
		return ResponseEntity.ok(applicationService.saveDraft(studentId, applicationId, req));
	}
	@Operation(summary = "지원서 제출", description = "SUBMITTED 로 상태를 전환합니다.")
	@PostMapping("/{applicationId}/submit")
	public ResponseEntity<ApplicationResponse> submit(
		@RequestParam Long studentId,
		@PathVariable Long applicationId
	) {
		return ResponseEntity.ok(applicationService.submit(studentId, applicationId));
	}
	@Operation(summary = "지원서 삭제", description = "상태의 지원서를 삭제합니다.")
	@DeleteMapping("/{applicationId}")
	public ResponseEntity<Void> delete(
		@RequestParam Long studentId,
		@PathVariable Long applicationId
	) {
		applicationService.deleteDraft(studentId, applicationId);
		return ResponseEntity.noContent().build();
	}
}

