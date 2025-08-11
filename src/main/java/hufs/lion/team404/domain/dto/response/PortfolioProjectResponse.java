package hufs.lion.team404.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PortfolioProjectResponse {
    private Long id;
    private String portfolio;
    private String title;
    private String introduction;
    private String outline;
    private String work;
    private String process;
    private String result;
    private String grow;
    private String competency;
    private String prize;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PortfolioProjectResponse(Long id, String portfolio, String title, String introduction, String outline,
                                    String work, String process, String result, String grow, String competency,
                                    String prize, Integer displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.portfolio = portfolio;
        this.title = title;
        this.introduction = introduction;
        this.outline = outline;
        this.work = work;
        this.process = process;
        this.result = result;
        this.grow = grow;
        this.competency = competency;
        this.prize = prize;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
