package hufs.lion.team404.service;

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
    public Long update(Long projectRequestId, Long userId, String title, String projectOverview, LocalDate startDate, LocalDate endDate, Integer estimatedDuration, String detailedTasks, String requiredSkills, Integer budget, String paymentMethod,
                       String workLocation, String workSchedule, String preferredMajor, Integer minGrade, String requiredExperience, String additionalNotes)
    {
        ProjectRequest projectRequest = projectRequestRepository.findById(projectRequestId)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서를 찾을 수 없습니다."));

        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Store store = user.getStore();
        if (store == null || !projectRequest.getStore().getId().equals(store.getId())) {
            throw new IllegalArgumentException("본인의 의뢰서만 수정할 수 있습니다.");
        }

        if (title != null)
            projectRequest.setTitle(title);
        if (projectOverview != null)
            projectRequest.setProjectOverview(projectOverview);
        if (startDate != null)
            projectRequest.setStartDate(startDate);
        if (endDate != null)
            projectRequest.setEndDate(endDate);
        if (estimatedDuration != null)
            projectRequest.setEstimatedDuration(estimatedDuration);
        if (detailedTasks != null)
            projectRequest.setDetailedTasks(detailedTasks);
        if (requiredSkills != null)
            projectRequest.setRequiredSkills(requiredSkills);
        if (budget != null)
            projectRequest.setBudget(budget);
        if (paymentMethod != null)
            projectRequest.setPaymentMethod(paymentMethod);
        if (workLocation != null)
            projectRequest.setWorkLocation(workLocation);
        if (workSchedule != null)
            projectRequest.setWorkSchedule(workSchedule);
        if (preferredMajor != null)
            projectRequest.setPreferredMajor(preferredMajor);
        if (minGrade != null)
            projectRequest.setMinGrade(minGrade);
        if (requiredExperience != null)
            projectRequest.setRequiredExperience(requiredExperience);
        if (additionalNotes != null)
            projectRequest.setAdditionalNotes(additionalNotes);

        return projectRequest.getId();

    }

    
    @Transactional
    public void deleteById(Long id) {
        projectRequestRepository.deleteById(id);
    }
}
