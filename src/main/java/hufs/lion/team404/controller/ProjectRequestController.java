package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.ProjectRequestCreateRequestDto;
import hufs.lion.team404.domain.dto.request.ProjectRequestUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.entity.ProjectRequest;
import hufs.lion.team404.model.ProjectRequestModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/projectRequest")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "의뢰서 제안", description = "의뢰서 제안 관련 API")
public class ProjectRequestController {
    private final ProjectRequestModel projectRequestModel;

    @PostMapping("/")
    @Operation(
            summary = "의뢰서 생성",
            description = "새로운 의뢰서를 생성합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Long> createProjectRequest(
            @Valid @RequestBody ProjectRequestCreateRequestDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long id = projectRequestModel.createProjectRequest(dto, userPrincipal.getEmail());
        return ApiResponse.success("의뢰가 생성되었습니다.", id);
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
    @PutMapping("/{projectRequestId}")
    @Operation(
            summary = "의뢰서 수정",
            description = "의뢰서를 수정합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<?> updateProjectRequest(
            @PathVariable("projectRequestId") Long projectRequestId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ProjectRequestUpdateRequestDto dto) {

        Long updatedId = projectRequestModel.update(
                projectRequestId, dto, userPrincipal.getId()
        ).getId();
        return ApiResponse.success(updatedId);
    }

    // 삭제
    @DeleteMapping("/{projectRequestId}")
    @Operation(
            summary = "의뢰서 삭제",
            description = "의뢰서를 삭제합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<?> deleteProjectRequest (
            @PathVariable("projectRequestId") Long projectRequestId,
            @AuthenticationPrincipal UserPrincipal authentication) {

        Long id = authentication.getId();
        projectRequestModel.deleteProjectRequest(projectRequestId, id);

        return ApiResponse.success("포트폴리오가 삭제되었습니다.");
    }
}
