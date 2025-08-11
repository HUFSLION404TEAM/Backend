package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.PortfolioCreateRequestDto;
import hufs.lion.team404.domain.dto.request.PortfolioProjectCreateRequestDto;
import hufs.lion.team404.domain.dto.request.PortfolioProjectUpdateRequestDto;
import hufs.lion.team404.domain.dto.request.PortfolioUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.PortfolioProjectResponse;
import hufs.lion.team404.domain.entity.PortfolioProject;
import hufs.lion.team404.model.PortfolioProjectModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import hufs.lion.team404.repository.PortfolioProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/mypage/portfolios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "포트폴리오 프로젝트", description =  "포트폴리오 프로젝트 관련 API")
public class PortfolioProjectController {
    private final PortfolioProjectModel portfolioProjectModel;

    // 생성
    @PostMapping("/{portfolioId}/projects")
    @Operation(
            summary = "포트폴리오 프로젝트 생성",
            description = "새로운 프로젝트를 생성합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Long> createProject(
            @PathVariable Long portfolioId,
            @Valid @RequestBody PortfolioProjectCreateRequestDto dto) {

        PortfolioProject saved = portfolioProjectModel.createPortfolioProject(portfolioId, dto);

        return ApiResponse.success("포트폴리오 프로젝트가 성공적으로 생성되었습니다", saved.getId());
    }

    // 조회
    @GetMapping("/{portfolioId}/projects")
    @Operation(summary = "포트폴리오 프로젝트 조회",
            description = "포트폴리오 프로젝트를 조회합니다.")
    public ApiResponse<List<PortfolioProjectResponse>> getPortfolioProjects(@PathVariable Long portfolioId)
    {
        List<PortfolioProjectResponse> projects = portfolioProjectModel.getPortfolioProjects(portfolioId);
        return ApiResponse.success("프로젝트 조회 성공", projects);
    }

    // 수정
    @PutMapping("/{portfolioId}/projects/{projectId}")
    @Operation(
            summary = "포트폴리오 프로젝트 수정",
            description = "포트폴리오 프로젝트를 수정합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Void> updatePortfolioProject(
            @PathVariable("portfolioId") Long portfolioId,
            @PathVariable("projectId") Long projectId,
            @Valid @RequestBody PortfolioProjectUpdateRequestDto dto) {

        portfolioProjectModel.UpdatePortfolioProject(portfolioId, projectId, dto);

        return ApiResponse.success("포트폴리오가 성공적으로 수정되었습니다.");
    }

    // 삭제
    @DeleteMapping("/{portfolioId}/projects/{projectId}")
    @Operation(
            summary = "포트폴리오 프로젝트 삭제",
            description = "포트폴리오 프로젝트를 삭제합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Void> deletePortfolioProject(
            @PathVariable("portfolioId") Long portfolioId,
            @PathVariable("projectId") Long projectId,
            @AuthenticationPrincipal UserPrincipal authentication) {

        String email = authentication.getEmail();
        portfolioProjectModel.deletePortfolioProject(portfolioId, projectId, email);

        return ApiResponse.success("포트폴리오 프로젝트가 삭제되었습니다.");
    }

}
