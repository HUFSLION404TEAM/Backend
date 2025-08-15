package hufs.lion.team404.domain.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectRequestUpdateRequestDto {
    private String title;
    private String projectOverview;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer estimatedDuration;
    private String detailedTasks;
    private String requiredSkills;
    private Integer budget;
    private String paymentMethod;
    private String workLocation;
    private String workSchedule;
    private String preferredMajor;
    private Integer minGrade;
    private String requiredExperience;
    private String additionalNotes;
}
