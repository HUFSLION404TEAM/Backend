package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.entity.ProjectRequest;
import hufs.lion.team404.model.ProjectRequestModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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

    // 생성
    @PostMapping("/")
    @Operation(
            summary = "의뢰서 생성",
            description = "새로운 의뢰서를 생성합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<?> createProjectRequest(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(value = "store") String store,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "projectOverview") String projectOverview,
            @RequestParam(value = "startDate")LocalDate startDate,
            @RequestParam(value = "endDate") LocalDate endDate,
            @RequestParam(value = "estimatedDuration") Integer estimatedDuration,
            @RequestParam(value = "detailedTasks") String detailedTasks,
            @RequestParam(value = "requiredSkills") String requiredSkills,
            @RequestParam(value = "budget") Integer budget,
            @RequestParam(value = "paymentMethod") String paymentMethod,
            @RequestParam(value = "workLocation") String workLocation,
            @RequestParam(value = "workSchedule") String workSchedule,
            @RequestParam(value = "preferredMajor", required = false) String preferredMajor,
            @RequestParam(value = "minGrade", required = false) Integer minGrade,
            @RequestParam(value = "requiredExperience", required = false) String requiredExperience,
            @RequestParam(value = "additionalNotes", required = false) String additionalNotes
            )
    {
        Long projectRequest_id = projectRequestModel.createProjectRequest(
                title, projectOverview, startDate, endDate, estimatedDuration, detailedTasks,
                requiredSkills, budget, paymentMethod, workLocation, workSchedule, preferredMajor, minGrade,
                requiredExperience, additionalNotes, userPrincipal.getId());
        return ApiResponse.success(projectRequest_id);
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
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "projectOverview", required = false) String projectOverview,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate",   required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "estimatedDuration", required = false) Integer estimatedDuration,
            @RequestParam(value = "detailedTasks",     required = false) String detailedTasks,
            @RequestParam(value = "requiredSkills",    required = false) String requiredSkills,
            @RequestParam(value = "budget",            required = false) Integer budget,
            @RequestParam(value = "paymentMethod",     required = false) String paymentMethod,
            @RequestParam(value = "workLocation",      required = false) String workLocation,
            @RequestParam(value = "workSchedule",      required = false) String workSchedule,
            @RequestParam(value = "preferredMajor",    required = false) String preferredMajor,
            @RequestParam(value = "minGrade",          required = false) Integer minGrade,
            @RequestParam(value = "requiredExperience",required = false) String requiredExperience,
            @RequestParam(value = "additionalNotes",   required = false) String additionalNotes) {

        Long updatedId = projectRequestModel.updateProjectRequest(
                projectRequestId,
                title, projectOverview, startDate, endDate, estimatedDuration, detailedTasks, requiredSkills,
                budget, paymentMethod, workLocation, workSchedule,preferredMajor, minGrade,
                requiredExperience, additionalNotes, userPrincipal.getId()
        );
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
