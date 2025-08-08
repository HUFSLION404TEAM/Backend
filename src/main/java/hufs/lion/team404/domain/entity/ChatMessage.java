package hufs.lion.team404.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private User sender;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MessageType messageType;

	@Column(columnDefinition = "TEXT")
	private String content;

	private String fileUrl;

	@Column(nullable = false)
	private Boolean isReadByStore = false;

	@Column(nullable = false)
	private Boolean isReadByStudent = false;

	@CreationTimestamp
	private LocalDateTime createdAt;

	public enum MessageType {
		TEXT, IMAGE, FILE, SYSTEM, ENTER, QUIT
	}

	@Builder
	public ChatMessage(ChatRoom chatRoom, User sender, MessageType messageType, String content, String fileUrl,
		Boolean isReadByStore, Boolean isReadByStudent) {
		this.chatRoom = chatRoom;
		this.sender = sender;
		this.messageType = messageType;
		this.content = content;
		this.fileUrl = fileUrl;
		this.isReadByStore = isReadByStore;
		this.isReadByStudent = isReadByStudent;
	}
}
