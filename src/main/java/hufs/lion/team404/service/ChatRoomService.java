package hufs.lion.team404.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hufs.lion.team404.domain.entity.ChatRoom;
import hufs.lion.team404.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public ChatRoom save(ChatRoom chatRoom) {
		return chatRoomRepository.save(chatRoom);
	}

	public Optional<ChatRoom> findById(Long id) {
		return chatRoomRepository.findById(id);
	}

	public Optional<ChatRoom> findByStoreUserIdAndStudentUserId(Long storeUserId, Long studentUserId) {
		return chatRoomRepository.findByStoreUserIdAndStudentUserId(storeUserId, studentUserId);
	}

	public List<ChatRoom> findByStoreUserIdOrderByLastMessageAtDesc(Long storeUserId) {
		return chatRoomRepository.findByStoreUserIdOrderByLastMessageAtDesc(storeUserId);
	}

	public List<ChatRoom> findByStudentUserIdOrderByLastMessageAtDesc(Long studentUserId) {
		return chatRoomRepository.findByStudentUserIdOrderByLastMessageAtDesc(studentUserId);
	}

	public List<ChatRoom> findByStoreUserIdOrStudentUserIdOrderByLastMessageAtDesc(Long storeUserId,
		Long studentUserId) {
		return chatRoomRepository.findByStoreUserIdOrStudentUserIdOrderByLastMessageAtDesc(storeUserId, studentUserId);
	}

	public List<ChatRoom> findAll() {
		return chatRoomRepository.findAll();
	}

	public List<ChatRoom> findByStore_BusinessNumberOrderByLastMessageAtDesc(String storeBusinessNumber) {
		return chatRoomRepository.findByStore_BusinessNumberOrderByLastMessageAtDesc(storeBusinessNumber);
	}

	@Transactional
	public void deleteById(Long id) {
		chatRoomRepository.deleteById(id);
	}
}
