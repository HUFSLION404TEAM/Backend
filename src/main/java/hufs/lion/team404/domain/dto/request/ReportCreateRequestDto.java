package hufs.lion.team404.domain.dto.request;

import hufs.lion.team404.domain.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "신고 작성 요청 DTO")
public class ReportCreateRequestDto {
    
    @NotNull(message = "매칭 ID는 필수입니다.")
    @Schema(description = "매칭 ID", example = "1")
    private Long matchingId;
    
    @NotNull(message = "신고 타입은 필수입니다.")
    @Schema(description = "신고 타입", example = "INAPPROPRIATE_BEHAVIOR")
    private Report.ReportType reportType;
    
    @NotBlank(message = "신고 내용은 필수입니다.")
    @Size(min = 10, max = 1000, message = "신고 내용은 10자 이상 1000자 이하여야 합니다.")
    @Schema(description = "신고 내용", example = "약속된 시간에 나타나지 않았습니다.")
    private String description;
}
