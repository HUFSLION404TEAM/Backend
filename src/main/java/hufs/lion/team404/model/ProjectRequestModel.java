package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.request.ProjectRequestCreateRequestDto;
import hufs.lion.team404.domain.dto.request.ProjectRequestUpdateRequestDto;
import hufs.lion.team404.domain.entity.*;
import hufs.lion.team404.exception.StoreNotFoundException;
import hufs.lion.team404.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProjectRequestModel {
    private final ProjectRequestService projectRequestService;
    private final UserService userService;
    private final StoreService storeService;

    public Long createProjectRequest(ProjectRequestCreateRequestDto dto, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Store store = storeService.findById(dto.getStoreId())
               .orElseThrow(() -> new StoreNotFoundException("Store not found: " + dto.getStoreId()));

        if (!store.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("본인 소유의 상점만 선택할 수 있습니다.");
        } else {
            store = user.getStore();
            if (store == null) {
                throw new StoreNotFoundException("해당 유저(" + user.getId() + ")에 등록된 상점이 없습니다.");
            }
        }

        LocalDate startDate = dto.getStartDate();
        LocalDate endDate   = dto.getEndDate();
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("마감일은 시작일보다 빠를 수 없습니다.");
        }

        ProjectRequest projectRequest = ProjectRequest.builder()
                .store(store)
                .title(dto.getTitle())
                .projectOverview(dto.getProjectOverview())
                .startDate(startDate)
                .endDate(endDate)
                .estimatedDuration(dto.getEstimatedDuration())
                .detailedTasks(dto.getDetailedTasks())
                .requiredSkills(dto.getRequiredSkills())
                .budget(dto.getBudget())
                .paymentMethod(dto.getPaymentMethod())
                .workLocation(dto.getWorkLocation())
                .workSchedule(dto.getWorkSchedule())
                .preferredMajor(dto.getPreferredMajor())
                .minGrade(dto.getMinGrade())
                .requiredExperience(dto.getRequiredExperience())
                .additionalNotes(dto.getAdditionalNotes())
                .status(ProjectRequest.Status.ACTIVE)
                .build();

        return projectRequestService.save(projectRequest).getId();
    }

    // 의뢰서 조회
    @Transactional(readOnly = true)
    public ProjectRequest getProjectRequest(Long id) {
        return projectRequestService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서를 찾을 수 없습니다."));
    }


    // 의뢰서 수정
    @Transactional
    public ProjectRequest update(Long projectRequestId, ProjectRequestUpdateRequestDto dto, Long userId) {

        ProjectRequest updated = projectRequestService.update(projectRequestId, dto, userId);
        return updated;
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
