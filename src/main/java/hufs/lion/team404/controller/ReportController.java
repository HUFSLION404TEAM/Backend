package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.ReportCreateRequestDto;
import hufs.lion.team404.domain.dto.request.ReportHandleRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.ReportResponse;
import hufs.lion.team404.model.ReportModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "신고", description = "신고 작성 및 관리 관련 API")
public class ReportController {
    
    private final ReportModel reportModel;
    
    @PostMapping
    @Operation(
            summary = "신고 작성",
            description = "매칭 상대방에 대해 신고를 작성합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Long> createReport(
            @Valid @RequestBody ReportCreateRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long reportId = reportModel.createReport(request, userPrincipal.getEmail());
        return ApiResponse.success("신고가 성공적으로 접수되었습니다.", reportId);
    }
    
    @GetMapping("/my")
    @Operation(
            summary = "내가 작성한 신고 조회",
            description = "현재 사용자가 작성한 신고 목록을 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<List<ReportResponse>> getMyReports(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<ReportResponse> reports = reportModel.getReportsByReporter(userPrincipal.getEmail());
        return ApiResponse.success("내 신고 목록을 성공적으로 조회했습니다.", reports);
    }
    
    @GetMapping("/admin/all")
    @Operation(
            summary = "모든 신고 조회 (관리자용)",
            description = "모든 신고를 조회합니다. 관리자 권한이 필요합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<List<ReportResponse>> getAllReports(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<ReportResponse> reports = reportModel.getAllReports(userPrincipal.getEmail());
        return ApiResponse.success("모든 신고를 성공적으로 조회했습니다.", reports);
    }
    
    @GetMapping("/admin/pending")
    @Operation(
            summary = "처리 대기 중인 신고 조회 (관리자용)",
            description = "처리 대기 중인 신고만 조회합니다. 관리자 권한이 필요합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<List<ReportResponse>> getPendingReports(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<ReportResponse> reports = reportModel.getPendingReports(userPrincipal.getEmail());
        return ApiResponse.success("처리 대기 중인 신고를 성공적으로 조회했습니다.", reports);
    }
    
    @PutMapping("/admin/{reportId}")
    @Operation(
            summary = "신고 처리 (관리자용)",
            description = "신고를 처리합니다. 관리자 권한이 필요합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Long> handleReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ReportHandleRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long id = reportModel.handleReport(reportId, request, userPrincipal.getEmail());
        return ApiResponse.success("신고가 성공적으로 처리되었습니다.", id);
    }
    
    @GetMapping("/{reportId}")
    @Operation(
            summary = "신고 상세 조회",
            description = "특정 신고의 상세 정보를 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<ReportResponse> getReport(
            @PathVariable Long reportId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        ReportResponse report = reportModel.getReport(reportId, userPrincipal.getEmail());
        return ApiResponse.success("신고 정보를 성공적으로 조회했습니다.", report);
    }
}
