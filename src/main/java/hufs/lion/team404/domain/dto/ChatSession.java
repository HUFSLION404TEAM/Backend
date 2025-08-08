package hufs.lion.team404.domain.dto;

import java.time.LocalDateTime;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class ChatSession {
	private WebSocketSession session;
	private Long userId;
	private Long chatRoomId;
	private LocalDateTime connectedAt;
}