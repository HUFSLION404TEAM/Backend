package hufs.lion.team404.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "찜하기/찜 취소 요청 DTO")
public class FavoriteToggleRequestDto {
    
    @Schema(description = "대상 ID (학생 ID 또는 가게 ID)", example = "1")
    private Long targetId;
    
    @Schema(description = "메모", example = "관심있는 학생입니다")
    private String memo;
}
