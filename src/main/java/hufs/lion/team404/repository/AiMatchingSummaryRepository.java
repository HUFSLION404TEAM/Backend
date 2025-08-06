package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.AiMatchingSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiMatchingSummaryRepository extends JpaRepository<AiMatchingSummary, Long> {
    
    Optional<AiMatchingSummary> findByMatchingId(Long matchingId);
    
    List<AiMatchingSummary> findByStudentId(Long studentId);
    
    List<AiMatchingSummary> findByGenerationStatus(AiMatchingSummary.GenerationStatus status);
    
    List<AiMatchingSummary> findByStudentIdOrderByGeneratedAtDesc(Long studentId);
    
    List<AiMatchingSummary> findByGenerationStatusOrderByCreatedAtAsc(AiMatchingSummary.GenerationStatus status);
}
