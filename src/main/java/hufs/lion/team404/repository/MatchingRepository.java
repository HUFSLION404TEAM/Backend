package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    
    List<Matching> findByProjectRequestId(Long projectRequestId);
    
    List<Matching> findByPortfolioId(Long portfolioId);
    
    List<Matching> findByStatus(Matching.Status status);
    
    List<Matching> findByMatchedBy(Matching.MatchedBy matchedBy);
    
    List<Matching> findByChatRoomId(Long chatRoomId);
    
    List<Matching> findByProjectRequestStoreUserIdOrderByCreatedAtDesc(Long storeUserId);
    
    List<Matching> findByPortfolioStudentUserIdOrderByCreatedAtDesc(Long studentUserId);
    
    // 채팅방의 학생으로 매칭 조회
    List<Matching> findByChatRoomStudentUserIdOrderByCreatedAtDesc(Long studentUserId);
}
