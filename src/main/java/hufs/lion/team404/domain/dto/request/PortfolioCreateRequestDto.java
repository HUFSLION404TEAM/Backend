package hufs.lion.team404.domain.dto.request;

import lombok.Getter;

@Getter
public class PortfolioCreateRequestDto {
    private Long studentId;
    private String studentName;
    private String title;
    private String region;
    private String representSentence;
    private String career;
    private Boolean isPublic;
    private Boolean isJobSeeking;
}
