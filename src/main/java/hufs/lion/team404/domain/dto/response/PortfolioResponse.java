package hufs.lion.team404.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String title;
    private String progressPeriod;
    private boolean prize;
    private String workDoneProgress;
    private String result;
    private String felt;
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imageUrls; // 이미지 URL 목록
}
