package hufs.lion.team404.service;

import hufs.lion.team404.entity.ChatMessage;
import hufs.lion.team404.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {
    
    private final ChatMessageRepository chatMessageRepository;
    
    @Transactional
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
    
    public Optional<ChatMessage> findById(Long id) {
        return chatMessageRepository.findById(id);
    }
    
    public List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }
    
    public List<ChatMessage> findByChatRoomIdAndIsReadByStoreFalse(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomIdAndIsReadByStoreFalse(chatRoomId);
    }
    
    public List<ChatMessage> findByChatRoomIdAndIsReadByStudentFalse(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomIdAndIsReadByStudentFalse(chatRoomId);
    }
    
    public long countByChatRoomIdAndIsReadByStoreFalse(Long chatRoomId) {
        return chatMessageRepository.countByChatRoomIdAndIsReadByStoreFalse(chatRoomId);
    }
    
    public long countByChatRoomIdAndIsReadByStudentFalse(Long chatRoomId) {
        return chatMessageRepository.countByChatRoomIdAndIsReadByStudentFalse(chatRoomId);
    }
    
    public List<ChatMessage> findAll() {
        return chatMessageRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        chatMessageRepository.deleteById(id);
    }
}
