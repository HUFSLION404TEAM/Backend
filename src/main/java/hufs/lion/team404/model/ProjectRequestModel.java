package hufs.lion.team404.model;

import hufs.lion.team404.domain.entity.*;
import hufs.lion.team404.exception.StoreNotFoundException;
import hufs.lion.team404.exception.StudentNotFoundException;
import hufs.lion.team404.service.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRequestModel {
    private final ProjectRequestService projectRequestService;
    private final MatchingService matchingService;
    private final UserService userService;


    public Long createProjectRequest(String title, String projectOverview, LocalDate startDate, LocalDate endDate, Integer estimatedDuration, String detailedTasks, String requiredSkills, Integer budget, String paymentMethod, String workLocation, String workSchedule, String preferredMajor, Integer minGrade, String requiredExperience, String additionalNotes, Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));


        Store store = user.getStore();
        if (store == null) {
            throw new StoreNotFoundException(user.getId()+"에 해당하는 상점이 없습니다.");
        }

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("마감일은 시작일보다 빠를 수 없습니다.");
        }

        ProjectRequest projectRequest = ProjectRequest.builder()
                .store(store)
                .title(title)
                .projectOverview(projectOverview)
                .startDate(startDate)
                .endDate(endDate)
                .estimatedDuration(estimatedDuration)
                .detailedTasks(detailedTasks)
                .requiredSkills(requiredSkills)
                .budget(budget)
                .paymentMethod(paymentMethod)
                .workLocation(workLocation)
                .workSchedule(workSchedule)
                .preferredMajor(preferredMajor)
                .minGrade(minGrade)
                .requiredExperience(requiredExperience)
                .additionalNotes(additionalNotes)
                .status(ProjectRequest.Status.ACTIVE)
                .build();

        ProjectRequest save = projectRequestService.save(projectRequest);
        return save.getId();
    }

    // 의뢰서 삭제
    public void deleteProjectRequest(Long projectRequestId, Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        Store store = user.getStore();
        if (store == null) {
            throw new StoreNotFoundException("상점을 찾을 수 없습니다.");
        }

        ProjectRequest projectRequest = projectRequestService.findById(projectRequestId)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서를 찾을 수 없습니다."));

        if(!projectRequest.getStore().getId().equals(store.getId())) {
            throw new IllegalArgumentException("본인의 의뢰서만 삭제할 수 있습니다.");
        }

        projectRequestService.deleteById(projectRequestId);
    }
}
