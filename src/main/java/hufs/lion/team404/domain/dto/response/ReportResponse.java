package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Report;
import hufs.lion.team404.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "신고 응답 DTO")
public class ReportResponse {
    
    @Schema(description = "신고 ID", example = "1")
    private Long id;
    
    @Schema(description = "매칭 ID", example = "1")
    private Long matchingId;
    
    @Schema(description = "신고자 ID", example = "1")
    private Long reporterId;
    
    @Schema(description = "신고자 이름", example = "김학생")
    private String reporterName;
    
    @Schema(description = "신고 대상자 ID", example = "2")
    private Long reportedId;
    
    @Schema(description = "신고 대상자 이름", example = "박사장")
    private String reportedName;
    
    @Schema(description = "신고자 타입", example = "STUDENT")
    private Report.ReporterType reporterType;
    
    @Schema(description = "신고 타입", example = "INAPPROPRIATE_BEHAVIOR")
    private Report.ReportType reportType;
    
    @Schema(description = "신고 내용", example = "약속된 시간에 나타나지 않았습니다.")
    private String description;
    
    @Schema(description = "신고 상태", example = "PENDING")
    private Report.Status status;
    
    @Schema(description = "관리자 응답", example = "확인 후 처리하겠습니다.")
    private String adminResponse;
    
    @Schema(description = "처리한 관리자 ID", example = "3")
    private Long handledBy;
    
    @Schema(description = "처리 완료 시간", example = "2024-08-17T15:00:00")
    private LocalDateTime resolvedAt;
    
    @Schema(description = "신고 시간", example = "2024-08-17T10:00:00")
    private LocalDateTime createdAt;
    
    public static ReportResponse fromEntity(Report report) {
        User reported = report.getReported();
        return new ReportResponse(
                report.getId(),
                report.getMatching().getId(),
                report.getReporter().getId(),
                report.getReporter().getName(),
                reported != null ? reported.getId() : null,
                reported != null ? reported.getName() : "알 수 없음",
                report.getReporterType(),
                report.getReportType(),
                report.getDescription(),
                report.getStatus(),
                report.getAdminResponse(),
                report.getHandledBy(),
                report.getResolvedAt(),
                report.getCreatedAt()
        );
    }
}
