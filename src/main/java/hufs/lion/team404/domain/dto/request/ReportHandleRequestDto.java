package hufs.lion.team404.domain.dto.request;

import hufs.lion.team404.domain.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "신고 처리 요청 DTO (관리자용)")
public class ReportHandleRequestDto {
    
    @NotNull(message = "신고 상태는 필수입니다.")
    @Schema(description = "처리 후 신고 상태", example = "RESOLVED")
    private Report.Status status;
    
    @Size(max = 1000, message = "관리자 응답은 1000자 이하여야 합니다.")
    @Schema(description = "관리자 응답", example = "신고 내용을 확인하여 적절히 처리하였습니다.")
    private String adminResponse;
}
