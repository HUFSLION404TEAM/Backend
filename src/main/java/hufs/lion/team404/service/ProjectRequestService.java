package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.ProjectRequest;
import hufs.lion.team404.repository.ProjectRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectRequestService {
    
    private final ProjectRequestRepository projectRequestRepository;
    
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
    public void deleteById(Long id) {
        projectRequestRepository.deleteById(id);
    }
}
