package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.RecruitingImage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RecruitingResponse {
    private Long id;
    private String title;
    private List<String> imagesUrl = new ArrayList<>();
    private String recruitmentPeriod;
    private String progressPeriod;

    private String price;

    private String projectOutline;
    private String expectedResults;
    private String detailRequirement;

    @Builder
    public RecruitingResponse(Long id, String title, List<String> imagesUrl,
                              String recruitmentPeriod, String progressPeriod, String price,
                              String projectOutline, String expectedResults, String detailRequirement) {
        this.id = id;
        this.title = title;
        this.imagesUrl = imagesUrl != null ? imagesUrl : new ArrayList<>();
        this.recruitmentPeriod = recruitmentPeriod;
        this.progressPeriod = progressPeriod;
        this.price = price;
        this.projectOutline = projectOutline;
        this.expectedResults = expectedResults;
        this.detailRequirement = detailRequirement;
    }

}
