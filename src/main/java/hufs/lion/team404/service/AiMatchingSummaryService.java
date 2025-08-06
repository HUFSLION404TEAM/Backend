package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.AiMatchingSummary;
import hufs.lion.team404.repository.AiMatchingSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiMatchingSummaryService {
    
    private final AiMatchingSummaryRepository aiMatchingSummaryRepository;
    
    @Transactional
    public AiMatchingSummary save(AiMatchingSummary aiMatchingSummary) {
        return aiMatchingSummaryRepository.save(aiMatchingSummary);
    }
    
    public Optional<AiMatchingSummary> findById(Long id) {
        return aiMatchingSummaryRepository.findById(id);
    }
    
    public Optional<AiMatchingSummary> findByMatchingId(Long matchingId) {
        return aiMatchingSummaryRepository.findByMatchingId(matchingId);
    }
    
    public List<AiMatchingSummary> findByStudentId(Long studentId) {
        return aiMatchingSummaryRepository.findByStudentId(studentId);
    }
    
    public List<AiMatchingSummary> findByGenerationStatus(AiMatchingSummary.GenerationStatus status) {
        return aiMatchingSummaryRepository.findByGenerationStatus(status);
    }
    
    public List<AiMatchingSummary> findByStudentIdOrderByGeneratedAtDesc(Long studentId) {
        return aiMatchingSummaryRepository.findByStudentIdOrderByGeneratedAtDesc(studentId);
    }
    
    public List<AiMatchingSummary> findByGenerationStatusOrderByCreatedAtAsc(AiMatchingSummary.GenerationStatus status) {
        return aiMatchingSummaryRepository.findByGenerationStatusOrderByCreatedAtAsc(status);
    }
    
    public List<AiMatchingSummary> findAll() {
        return aiMatchingSummaryRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        aiMatchingSummaryRepository.deleteById(id);
    }
}
