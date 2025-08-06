package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
    
    List<ChatMessage> findByChatRoomIdAndIsReadByStoreFalse(Long chatRoomId);
    
    List<ChatMessage> findByChatRoomIdAndIsReadByStudentFalse(Long chatRoomId);
    
    long countByChatRoomIdAndIsReadByStoreFalse(Long chatRoomId);
    
    long countByChatRoomIdAndIsReadByStudentFalse(Long chatRoomId);
}
