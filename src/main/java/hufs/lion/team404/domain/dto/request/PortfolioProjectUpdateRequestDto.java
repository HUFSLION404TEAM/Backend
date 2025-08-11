package hufs.lion.team404.domain.dto.request;

import lombok.Getter;

@Getter
public class PortfolioProjectUpdateRequestDto {
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
    private Boolean isPublic;
}
