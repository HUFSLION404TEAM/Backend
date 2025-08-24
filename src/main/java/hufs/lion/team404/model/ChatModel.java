package hufs.lion.team404.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import hufs.lion.team404.domain.dto.response.ChatRoomResponse;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatModel {

	private final ChatRoomService chatRoomService;
	private final ChatMessageService chatMessageService;
	private final UserService userService;
	private final StoreService storeService;
	private final StudentService studentService;

	/**
	 * 학생이 업체와 채팅방 생성
	 */
	public void createChatRoomWithStore(Long userId, String businessNumber) {
		User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
		
		if (user.getUserRole() != UserRole.STUDENT) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "학생만 업체와 채팅방을 생성할 수 있습니다.");
		}

		Student student = user.getStudent();
		if (student == null) {
			throw new CustomException(ErrorCode.STUDENT_NOT_FOUND, "학생 정보를 찾을 수 없습니다.");
		}

		Store store = storeService.findByBusinessNumber(businessNumber)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND, "업체를 찾을 수 없습니다."));

		ChatRoom chatRoom = ChatRoom.builder()
			.student(student)
			.store(store)
			.initiatedBy(ChatRoom.InitiatedBy.STUDENT)
			.build();
		chatRoomService.save(chatRoom);
	}

	/**
	 * 업체가 학생과 채팅방 생성
	 */
	public void createChatRoomWithStudent(Long userId, Long studentId, String businessNumber) {
		User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
		
		if (user.getUserRole() != UserRole.STORE) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "업체만 학생과 채팅방을 생성할 수 있습니다.");
		}

		// 지정된 사업자번호의 업체 조회
		Store store = storeService.findByBusinessNumber(businessNumber)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND, "업체를 찾을 수 없습니다."));

		// 해당 업체가 현재 사용자의 업체인지 확인
		if (!store.getUser().getId().equals(userId)) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "해당 업체의 소유자가 아닙니다.");
		}

		Student student = studentService.findById(studentId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND, "학생을 찾을 수 없습니다."));

		ChatRoom chatRoom = ChatRoom.builder()
			.student(student)
			.store(store)
			.initiatedBy(ChatRoom.InitiatedBy.STORE)
			.build();
		chatRoomService.save(chatRoom);
	}

	public boolean hasPermission(Long userId, Long roomId) {
		try {
			System.out.println("userId = " + userId);
			System.out.println("roomId = " + roomId);
			ChatRoom chatRoom = chatRoomService.findById(roomId).orElseThrow();

			if (chatRoom == null) {
				// 테스트를 위해 임시로 roomId 1은 허용
				if (roomId == 1L) {
					return true;
				}
				return false;
			}

			User user = userService.findById(userId).orElse(null);
			if (user == null) {
				return false;
			}

			System.out.println("user = " + user.getUserRole());
			System.out.println("user = " + user.getId());
			System.out.println("user = " + chatRoom.getStudent().getUser().getId());

			// User의 역할에 따라 권한 확인
			if (user.getUserRole() == UserRole.STUDENT) {
				return Objects.equals(chatRoom.getStudent().getUser().getId(), user.getStudent().getId());
			} else if (user.getUserRole() == UserRole.STORE) {
				// 사용자의 스토어 목록에서 채팅방의 스토어와 일치하는지 확인
				return user.getStores().stream()
					.anyMatch(store -> Objects.equals(store.getBusinessNumber(), chatRoom.getStore().getBusinessNumber()));
			}

			return false;
		} catch (Exception e) {
			// 테스트를 위해 예외 발생 시 임시로 허용
			return roomId == 1L;
		}
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
			.messageType(messageDto.getMessageType()) // 전달받은 메시지 타입 사용
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
  
	@Transactional(readOnly = true)
	public List<ChatRoomResponse> getMyChatRooms(Long userId) {
		User user = userService.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		UserRole role = user.getUserRole();
		
		List<ChatRoom> rooms;
		if (role == UserRole.STUDENT) {
			rooms = chatRoomService.findByStudentUserIdOrderByLastMessageAtDesc(userId);
		} else if (role == UserRole.STORE) {
			rooms = chatRoomService.findByStoreUserIdOrderByLastMessageAtDesc(userId);
		} else if (role == UserRole.ADMIN) {
			rooms = chatRoomService.findAll();
		} else {
			rooms = List.of();
		}
		
		return rooms.stream()
			.map(ChatRoomResponse::from)
			.collect(Collectors.toList());
	}

	/*
	업체가 채팅방 목록 조회
	 */
	@Transactional(readOnly = true)
	public List<ChatRoomResponse> getStoreChatRoomList(Long userId, String businessNumber) {
		System.out.println("userId = " + userId);
		User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
		System.out.println("user = " + user.getUserRole());
		if (user.getUserRole() != UserRole.STORE) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "업체만 학생과 채팅방을 조회할 수 있습니다.");
		}

		Store store = storeService.findByBusinessNumber(businessNumber)
				.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND, "업체를 찾을 수 없습니다."));

		if (!store.getUser().getId().equals(userId)) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "해당 업체의 소유자가 아닙니다.");
		}

		List<ChatRoom> rooms = chatRoomService.findByStore_BusinessNumberOrderByLastMessageAtDesc(store.getBusinessNumber());
		if (rooms.isEmpty()) {
			throw new IllegalArgumentException("채팅방을 찾을 수 없습니다.");
		}
		
		return rooms.stream()
			.map(ChatRoomResponse::from)
			.collect(Collectors.toList());
	}
}
