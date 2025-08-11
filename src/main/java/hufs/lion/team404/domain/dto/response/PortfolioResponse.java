package hufs.lion.team404.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PortfolioResponse {
    private Long id;
    private String title;
    private String region;
    private String representSentence;
    private String career;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PortfolioResponse(Long id, String title, String region, String representSentence, String career, Boolean isPublic, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.region = region;
        this.representSentence = representSentence;
        this.career = career;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
