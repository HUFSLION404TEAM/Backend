package hufs.lion.team404.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;

import hufs.lion.team404.domain.entity.ChatMessage;
import hufs.lion.team404.domain.entity.ChatRoom;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.exception.StoreNotFoundException;
import hufs.lion.team404.exception.UserNotFoundException;
import hufs.lion.team404.service.ChatMessageService;
import hufs.lion.team404.service.ChatRoomService;
import hufs.lion.team404.service.StoreService;
import hufs.lion.team404.service.StudentService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatModel {

	private final ChatRoomService chatRoomService;
	private final ChatMessageService chatMessageService;
	private final UserService userService;
	private final StoreService storeService;
	private final StudentService studentService;

	public void createChatRoom(Long user_id, Long target_id) {
		User user = userService.findById(user_id).orElseThrow(() -> new UserNotFoundException("User not found"));
		switch (user.getUserRole()) {
			case STORE -> {
				Store store = user.getStore();
				Student student = studentService.findById(target_id)
					.orElseThrow(() -> new StoreNotFoundException("Store not found"));

				ChatRoom chatRoom = ChatRoom.builder()
					.student(student)
					.store(store)
					.initiatedBy(ChatRoom.InitiatedBy.STUDENT)
					.build();

				chatRoomService.save(chatRoom);
			}

			case STUDENT -> {
				Student student = user.getStudent();
				Store store = storeService.findById(target_id)
					.orElseThrow(() -> new StoreNotFoundException("Store not found"));

				ChatRoom chatRoom = ChatRoom.builder()
					.student(student)
					.store(store)
					.initiatedBy(ChatRoom.InitiatedBy.STUDENT)
					.build();
				chatRoomService.save(chatRoom);
			}

		}

	}

	public boolean hasPermission(Long userId, Long roomId) {
		ChatRoom chatRoom = chatRoomService.findById(roomId)
			.orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

		return Objects.equals(chatRoom.getStudent().getId(), userId) || Objects.equals(chatRoom.getStore().getId(),
			userId);
	}

	public ChatMessage saveMessageToDatabase(ChatMessage messageDto, Long userId, Long chatRoomId) {
		// 연관 엔티티 조회
		ChatRoom chatRoom = chatRoomService.findById(chatRoomId)
			.orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
		User sender = userService.findById(userId)
			.orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage()));

		// 메시지 정보 설정
		ChatMessage chatMessage = ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(sender)
			.messageType(ChatMessage.MessageType.TEXT)
			.content(messageDto.getContent())
			.build();

		// 읽음 상태 초기 설정 (발신자는 읽음 처리)
		if (sender.getUserRole() == UserRole.STORE) {
			chatMessage.setIsReadByStore(true);
			chatMessage.setIsReadByStudent(false);
		} else {
			chatMessage.setIsReadByStore(false);
			chatMessage.setIsReadByStudent(true);
		}

		// DB에 저장
		ChatMessage savedMessage = chatMessageService.save(chatMessage);

		// 채팅방의 마지막 메시지 시간 업데이트
		chatRoom.setLastMessageAt(LocalDateTime.now());
		chatRoomService.save(chatRoom);

		return savedMessage;
	}
}
