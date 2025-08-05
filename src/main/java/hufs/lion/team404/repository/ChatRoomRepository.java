package hufs.lion.team404.repository;

import hufs.lion.team404.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    Optional<ChatRoom> findByStoreUserIdAndStudentUserId(Long storeUserId, Long studentUserId);
    
    List<ChatRoom> findByStoreUserIdOrderByLastMessageAtDesc(Long storeUserId);
    
    List<ChatRoom> findByStudentUserIdOrderByLastMessageAtDesc(Long studentUserId);
    
    List<ChatRoom> findByStoreUserIdOrStudentUserIdOrderByLastMessageAtDesc(Long storeUserId, Long studentUserId);
}
