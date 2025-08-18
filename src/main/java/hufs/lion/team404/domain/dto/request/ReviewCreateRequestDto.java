package hufs.lion.team404.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "리뷰 작성 요청 DTO")
public class ReviewCreateRequestDto {
    
    @NotNull(message = "매칭 ID는 필수입니다.")
    @Schema(description = "매칭 ID", example = "1")
    private Long matchingId;
    
    @NotNull(message = "별점은 필수입니다.")
    @Min(value = 0, message = "별점은 0 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하여야 합니다.")
    @Schema(description = "별점 (0~5)", example = "4")
    private Integer rating;
    
    @Size(max = 1000, message = "리뷰 내용은 1000자 이하여야 합니다.")
    @Schema(description = "리뷰 내용", example = "정말 만족스러운 작업이었습니다!")
    private String content;
}
