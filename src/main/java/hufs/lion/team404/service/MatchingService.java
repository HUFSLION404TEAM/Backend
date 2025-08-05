package hufs.lion.team404.service;

import hufs.lion.team404.entity.Matching;
import hufs.lion.team404.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingService {
    
    private final MatchingRepository matchingRepository;
    
    @Transactional
    public Matching save(Matching matching) {
        return matchingRepository.save(matching);
    }
    
    public Optional<Matching> findById(Long id) {
        return matchingRepository.findById(id);
    }
    
    public List<Matching> findByProjectRequestId(Long projectRequestId) {
        return matchingRepository.findByProjectRequestId(projectRequestId);
    }
    
    public List<Matching> findByPortfolioId(Long portfolioId) {
        return matchingRepository.findByPortfolioId(portfolioId);
    }
    
    public List<Matching> findByStatus(Matching.Status status) {
        return matchingRepository.findByStatus(status);
    }
    
    public List<Matching> findByMatchedBy(Matching.MatchedBy matchedBy) {
        return matchingRepository.findByMatchedBy(matchedBy);
    }
    
    public List<Matching> findByChatRoomId(Long chatRoomId) {
        return matchingRepository.findByChatRoomId(chatRoomId);
    }
    
    public List<Matching> findByProjectRequestStoreUserIdOrderByCreatedAtDesc(Long storeUserId) {
        return matchingRepository.findByProjectRequestStoreUserIdOrderByCreatedAtDesc(storeUserId);
    }
    
    public List<Matching> findByPortfolioStudentUserIdOrderByCreatedAtDesc(Long studentUserId) {
        return matchingRepository.findByPortfolioStudentUserIdOrderByCreatedAtDesc(studentUserId);
    }
    
    public List<Matching> findAll() {
        return matchingRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        matchingRepository.deleteById(id);
    }
}
