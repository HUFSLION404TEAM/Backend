package hufs.lion.team404.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import hufs.lion.team404.domain.dto.ChatSession;
import hufs.lion.team404.domain.entity.ChatMessage;
import hufs.lion.team404.model.ChatModel;
import hufs.lion.team404.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler implements WebSocketHandler {

	private final ObjectMapper mapper;
	private final Map<String, ChatSession> sessionMap = new HashMap<>(); // 세션ID -> ChatSession
	private final Map<Long, Set<String>> chatRoomSessionMap = new HashMap<>();
	private final Map<Long, Set<String>> userSessionMap = new HashMap<>(); // 유저ID -> 세션ID들 (다중기기 지원)
	private final JwtTokenProvider jwtTokenProvider;
	private final ChatModel chatModel;

	// 소켓 연결 성공
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		HttpHeaders headers = session.getHandshakeHeaders();
		String authHeader = headers.getFirst("Authorization");
		String roomIdHeader = headers.getFirst("Room-Id");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			log.warn("Invalid Authorization header");
			session.close(CloseStatus.NOT_ACCEPTABLE);
			return;
		}

		String token = authHeader.substring(7);
		Long userId = jwtTokenProvider.getUserId(token);
		Long roomId = Long.parseLong(roomIdHeader);

		// 채팅방 접근 권한 확인
		if (!chatModel.hasPermission(userId, roomId)) {
			log.warn("User {} has no permission to room {}", userId, roomId);
			session.close(CloseStatus.NOT_ACCEPTABLE);
			return;
		}

		// ChatSession 생성 및 저장
		ChatSession chatSession = new ChatSession();
		chatSession.setSession(session);
		chatSession.setUserId(userId);
		chatSession.setChatRoomId(roomId);
		chatSession.setConnectedAt(LocalDateTime.now());

		sessionMap.put(session.getId(), chatSession);

		// 채팅방에 세션 추가
		chatRoomSessionMap.computeIfAbsent(roomId, k -> new HashSet<>())
			.add(session.getId());

		log.info("User {} connected to room {} with session {}", userId, roomId, session.getId());
	}

	// 소켓 통신 시 메세지 전송을 다루는 부분
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		String payload = (String)message.getPayload();
		log.info("payload {}", payload);

		// 페이로드 -> chatMessageDto 변환
		ChatMessage chatMessageDto = mapper.readValue(payload, ChatMessage.class);

		// 세션에서 사용자 정보 가져오기
		ChatSession chatSession = sessionMap.get(session.getId());
		if (chatSession == null) {
			log.warn("세션 정보를 찾을 수 없습니다: {}", session.getId());
			return;
		}

		Long chatRoomId = chatSession.getChatRoomId();
		Long userId = chatSession.getUserId();

		// 채팅방과 연결된 모든 세션ID 가져오기
		Set<String> chatRoomSessionIds = chatRoomSessionMap.get(chatRoomId);

		if (chatMessageDto.getMessageType().equals(ChatMessage.MessageType.TEXT) ||
			chatMessageDto.getMessageType().equals(ChatMessage.MessageType.IMAGE) ||
			chatMessageDto.getMessageType().equals(ChatMessage.MessageType.FILE)) {

			ChatMessage savedMessage = chatModel.saveMessageToDatabase(chatMessageDto, userId, chatRoomId);

			// 저장된 메시지를 다시 클라이언트들에게 전송 (ID 포함해서)
			sendMessageToChatRoom(savedMessage, chatRoomSessionIds);

		} else if (chatMessageDto.getMessageType().equals(ChatMessage.MessageType.ENTER)) {
			// 시스템 메시지 (DB 저장 안함)
			sendMessageToChatRoom(chatMessageDto, chatRoomSessionIds);
		}

		if (chatRoomSessionIds.size() >= 3) {
			removeClosedSession(chatRoomSessionIds);
		}
	}

	// 연결이 끊어진 세션 삭제
	private void removeClosedSession(Set<String> sessionIds) {
		sessionIds.removeIf(sessionId -> !sessionMap.containsKey(sessionId));
	}

	// 세션에게 메세지 전송
	private void sendMessageToChatRoom(ChatMessage chatMessageDto, Set<String> sessionIds) {
		sessionIds.parallelStream()
			.map(sessionMap::get)
			.filter(Objects::nonNull)
			.forEach(chatSession -> sendMessage(chatSession.getSession(), chatMessageDto));
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage(new TextMessage(mapper.writeValueAsBytes(message)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		ChatSession chatSession = sessionMap.remove(session.getId());
		if (chatSession != null) {
			// 채팅방에서 세션 제거
			if (chatSession.getChatRoomId() != null) {
				Set<String> roomSessions = chatRoomSessionMap.get(chatSession.getChatRoomId());
				if (roomSessions != null) {
					roomSessions.remove(session.getId());
				}
			}

			// 유저 세션에서도 제거
			Set<String> userSessions = userSessionMap.get(chatSession.getUserId());
			if (userSessions != null) {
				userSessions.remove(session.getId());
			}
		}
		log.info("{} 연결 끊김", session.getId());
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}