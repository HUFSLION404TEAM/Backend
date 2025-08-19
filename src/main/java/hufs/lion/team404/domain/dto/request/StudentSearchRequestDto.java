package hufs.lion.team404.domain.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentSearchRequestDto {
    private String region;
    private String career;
    private Boolean isEmployment;
    private String keyword;
}