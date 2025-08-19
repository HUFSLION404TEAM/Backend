package hufs.lion.team404.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PortfolioUpdateResponse {
    private String title;
    private String progressPeriod;
    private boolean isPrize;
    private String workDoneProgress;
    private String result;
    private String felt;
    private boolean isPrivate;

    @Builder
    public PortfolioUpdateResponse(String title, String progressPeriod, boolean isPrize, String workDoneProgress, String result, String felt, boolean isPrivate) {
        this.title = title;
        this.progressPeriod = progressPeriod;
        this.isPrize = isPrize;
        this.workDoneProgress = workDoneProgress;
        this.result = result;
        this.felt = felt;
        this.isPrivate = isPrivate;
    }
}
