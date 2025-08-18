package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    
    List<Matching> findByProjectRequestId(Long projectRequestId);

    List<Matching> findByStatus(Matching.Status status);
    
    List<Matching> findByMatchedBy(Matching.MatchedBy matchedBy);
    
    List<Matching> findByChatRoomId(Long chatRoomId);
    
    List<Matching> findByProjectRequestStoreUserIdOrderByCreatedAtDesc(Long storeUserId);

    // 채팅방의 학생으로 매칭 조회
    List<Matching> findByChatRoomStudentUserIdOrderByCreatedAtDesc(Long studentUserId);

    List<Matching> findByChatRoomStoreBusinessNumberOrderByCreatedAtDesc(String businessNumber);

    // 채팅방의 업체 유저로 매칭 조회
    List<Matching> findByChatRoomStoreUserIdOrderByCreatedAtDesc(Long userId);

    // 업체별 매칭 수 조회
    int countByChatRoomStore(Store store);

    // 업체별 특정 상태의 매칭 수 조회
    int countByChatRoomStoreAndStatus(Store store, Matching.Status status);
}
