package hufs.lion.team404.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import java.time.LocalDate;

@Getter
public class ProjectRequestCreateRequestDto {
    @NotNull
    private Long storeId;
    @NotBlank
    private String title;
    private String projectOverview;
    private LocalDate startDate;
    private LocalDate endDate;
    @Positive
    private Integer estimatedDuration;
    private String detailedTasks;
    private String requiredSkills;
    @Positive
    private Integer budget;
    private String paymentMethod;
    private String workLocation;
    private String workSchedule;
    private String preferredMajor;
    @Min(1) @Max(4)
    private Integer minGrade;
    private String requiredExperience;
    private String additionalNotes;

}
