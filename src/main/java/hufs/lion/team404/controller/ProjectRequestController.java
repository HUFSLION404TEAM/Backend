package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.model.ProjectRequestModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
