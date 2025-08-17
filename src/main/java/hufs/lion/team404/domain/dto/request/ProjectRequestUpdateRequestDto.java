package hufs.lion.team404.domain.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectRequestUpdateRequestDto {
    @NotBlank
    private String title;
    private String projectOverview;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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
