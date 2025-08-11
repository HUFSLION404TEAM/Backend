package hufs.lion.team404.domain.dto;

import java.time.LocalDateTime;

import hufs.lion.team404.domain.entity.ChatMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageDto {
	private Long id;
	private String content;
	private ChatMessage.MessageType messageType;
	private String imageUrl;
	private String fileName;
	private Long senderId;
	private String senderName;
	private Long chatRoomId;
	private Boolean isReadByStore;
	private Boolean isReadByStudent;
	private LocalDateTime createdAt;

	@Builder
	public ChatMessageDto(Long id, String content, ChatMessage.MessageType messageType,
		String imageUrl, String fileName, Long senderId, String senderName,
		Long chatRoomId, Boolean isReadByStore, Boolean isReadByStudent,
		LocalDateTime createdAt) {
		this.id = id;
		this.content = content;
		this.messageType = messageType;
		this.imageUrl = imageUrl;
		this.fileName = fileName;
		this.senderId = senderId;
		this.senderName = senderName;
		this.chatRoomId = chatRoomId;
		this.isReadByStore = isReadByStore;
		this.isReadByStudent = isReadByStudent;
		this.createdAt = createdAt;
	}

	// Entity를 DTO로 변환하는 정적 메서드
	public static ChatMessageDto fromEntity(ChatMessage entity) {
		return ChatMessageDto.builder()
			.id(entity.getId())
			.content(entity.getContent())
			.messageType(entity.getMessageType())
			.imageUrl(entity.getFileUrl()) // fileUrl을 imageUrl로 매핑
			.fileName(entity.getFileUrl()) // fileUrl을 fileName으로도 매핑
			.senderId(entity.getSender().getId())
			.senderName(entity.getSender().getName())
			.chatRoomId(entity.getChatRoom().getId())
			.isReadByStore(entity.getIsReadByStore())
			.isReadByStudent(entity.getIsReadByStudent())
			.createdAt(entity.getCreatedAt())
			.build();
	}
}
