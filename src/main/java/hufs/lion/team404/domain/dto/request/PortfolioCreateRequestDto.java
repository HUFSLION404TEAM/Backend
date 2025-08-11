package hufs.lion.team404.domain.dto.request;

import lombok.Getter;

@Getter
public class PortfolioCreateRequestDto {
    private Long studentId;
    private String title;
    private String region;
    private String representSentence;
    private String career;

}
