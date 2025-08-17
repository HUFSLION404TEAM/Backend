package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.request.ProjectRequestCreateRequestDto;
import hufs.lion.team404.domain.dto.request.ProjectRequestUpdateRequestDto;
import hufs.lion.team404.domain.entity.*;
import hufs.lion.team404.exception.StoreNotFoundException;
import hufs.lion.team404.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRequestModel {
    private final ProjectRequestService projectRequestService;
    private final ProjectRequestFileService projectRequestFileService;
    private final UserService userService;
    private final StoreService storeService;
    private final MatchingService matchingService;


    @Transactional
    public Long createProjectRequest(ProjectRequestCreateRequestDto dto, String email, List<MultipartFile> files) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Store store = storeService.findById(dto.getStoreId())
               .orElseThrow(() -> new StoreNotFoundException("Store not found: " + dto.getStoreId()));

        if (!store.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("본인 소유의 상점만 선택할 수 있습니다.");
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

        ProjectRequest savedProjectRequest = projectRequestService.save(projectRequest);

        // ProjectRequest 생성 시 Matching도 함께 생성
        Matching matching = new Matching();
        matching.setProjectRequest(savedProjectRequest);
        matching.setMatchedBy(Matching.MatchedBy.STORE_OFFER); // 가게에서 의뢰한 경우
        matching.setStatus(Matching.Status.PENDING);
        matching.setOfferedAt(java.time.LocalDateTime.now());

        matchingService.save(matching);

        // 첨부파일 추가 관련 로직
        projectRequestFileService.update(projectRequest, files);

        return projectRequest.getId();
    }

    // 의뢰서 조회
    @Transactional(readOnly = true)
    public ProjectRequest getProjectRequest(Long id) {
        return projectRequestService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서를 찾을 수 없습니다."));
    }


    // 의뢰서 수정
    @Transactional
    public ProjectRequest update(Long projectRequestId, ProjectRequestUpdateRequestDto dto, Long userId,
                                 List<MultipartFile> files, boolean clearFiles) {

        ProjectRequest updated = projectRequestService.update(projectRequestId, dto, userId);

        if (clearFiles) {
            projectRequestFileService.deleteAllByProjectRequestId(updated.getId());

            if (files != null && !files.isEmpty()) {
                projectRequestFileService.update(updated, files);
            }
        }

        projectRequestFileService.update(updated, files);
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
