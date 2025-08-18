package hufs.lion.team404.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Getter
@Setter
public class ProjectRequestCreateRequestDto {

    @NotNull(message = "채팅방 ID는 필수입니다")
    private Long chatRoomId;

    private String title;
    private String projectOverview;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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
