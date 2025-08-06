package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.ProjectRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {
    
    List<ProjectRequest> findByStoreId(Long storeId);
    
    List<ProjectRequest> findByStatus(ProjectRequest.Status status);
    
    List<ProjectRequest> findByTitleContaining(String title);
    
    List<ProjectRequest> findByPreferredMajor(String preferredMajor);
    
    List<ProjectRequest> findByBudgetBetween(Integer minBudget, Integer maxBudget);
    
    List<ProjectRequest> findByStatusOrderByCreatedAtDesc(ProjectRequest.Status status);
}
