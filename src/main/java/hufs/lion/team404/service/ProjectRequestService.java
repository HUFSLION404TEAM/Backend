package hufs.lion.team404.service;

import hufs.lion.team404.domain.dto.request.ProjectRequestUpdateRequestDto;
import hufs.lion.team404.domain.entity.ProjectRequest;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.repository.ProjectRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectRequestService {
    
    private final ProjectRequestRepository projectRequestRepository;
    private final UserService userService;
    
    @Transactional
    public ProjectRequest save(ProjectRequest projectRequest) {
        return projectRequestRepository.save(projectRequest);
    }
    
    public Optional<ProjectRequest> findById(Long id) {
        return projectRequestRepository.findById(id);
    }
    
    public List<ProjectRequest> findByStoreId(Long storeId) {
        return projectRequestRepository.findByStoreId(storeId);
    }
    
    public List<ProjectRequest> findByStatus(ProjectRequest.Status status) {
        return projectRequestRepository.findByStatus(status);
    }
    
    public List<ProjectRequest> findByTitleContaining(String title) {
        return projectRequestRepository.findByTitleContaining(title);
    }
    
    public List<ProjectRequest> findByPreferredMajor(String preferredMajor) {
        return projectRequestRepository.findByPreferredMajor(preferredMajor);
    }
    
    public List<ProjectRequest> findByBudgetBetween(Integer minBudget, Integer maxBudget) {
        return projectRequestRepository.findByBudgetBetween(minBudget, maxBudget);
    }
    
    public List<ProjectRequest> findByStatusOrderByCreatedAtDesc(ProjectRequest.Status status) {
        return projectRequestRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public List<ProjectRequest> findAll() {
        return projectRequestRepository.findAll();
    }

    @Transactional
    public ProjectRequest update(Long projectRequestId, ProjectRequestUpdateRequestDto dto, Long userId)
    {
        ProjectRequest projectRequest = projectRequestRepository.findById(projectRequestId)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서를 찾을 수 없습니다."));

        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Store store = user.getStore();
        if (store == null || !projectRequest.getStore().getId().equals(store.getId())) {
            throw new IllegalArgumentException("본인의 의뢰서만 수정할 수 있습니다.");
        }

        LocalDate newStartDate = dto.getStartDate() != null ? dto.getStartDate() : projectRequest.getStartDate();
        LocalDate newEndDate   = dto.getEndDate()   != null ? dto.getEndDate()   : projectRequest.getEndDate();
        if (newStartDate != null && newEndDate != null && newEndDate.isBefore(newStartDate)) {
            throw new IllegalArgumentException("마감일은 시작일보다 빠를 수 없습니다.");
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank())
            projectRequest.setTitle(dto.getTitle());
        if (dto.getProjectOverview() != null)
            projectRequest.setProjectOverview(dto.getProjectOverview());
        if (dto.getStartDate() != null)
            projectRequest.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null)
            projectRequest.setEndDate(dto.getEndDate());
        if (dto.getEstimatedDuration() != null)
            projectRequest.setEstimatedDuration(dto.getEstimatedDuration());
        if (dto.getDetailedTasks() != null)
            projectRequest.setDetailedTasks(dto.getDetailedTasks());
        if (dto.getRequiredSkills() != null)
            projectRequest.setRequiredSkills(dto.getRequiredSkills());
        if (dto.getBudget() != null)
            projectRequest.setBudget(dto.getBudget());
        if (dto.getPaymentMethod() != null)
            projectRequest.setPaymentMethod(dto.getPaymentMethod());
        if (dto.getWorkLocation() != null)
            projectRequest.setWorkLocation(dto.getWorkLocation());
        if (dto.getWorkSchedule() != null)
            projectRequest.setWorkSchedule(dto.getWorkSchedule());
        if (dto.getPreferredMajor() != null)
            projectRequest.setPreferredMajor(dto.getPreferredMajor());
        if (dto.getMinGrade() != null)
            projectRequest.setMinGrade(dto.getMinGrade());
        if (dto.getRequiredExperience() != null)
            projectRequest.setRequiredExperience(dto.getRequiredExperience());
        if (dto.getAdditionalNotes() != null)
            projectRequest.setAdditionalNotes(dto.getAdditionalNotes());

        return projectRequest;

    }

    
    @Transactional
    public void deleteById(Long id) {
        projectRequestRepository.deleteById(id);
    }
}
