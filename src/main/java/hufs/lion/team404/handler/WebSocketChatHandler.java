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

import hufs.lion.team404.domain.dto.ChatMessageDto;
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
		// 먼저 헤더에서 확인
		HttpHeaders headers = session.getHandshakeHeaders();
		String authHeader = headers.getFirst("Authorization");
		String roomIdHeader = headers.getFirst("Room-Id");
		
		String token = null;
		Long roomId = null;
		
		// 헤더에서 토큰을 찾지 못한 경우 쿼리 파라미터에서 확인
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			String uri = session.getUri().toString();
			log.info("WebSocket URI: {}", uri);
			
			// 쿼리 파라미터에서 토큰과 룸ID 추출
			String query = session.getUri().getQuery();
			if (query != null) {
				String[] params = query.split("&");
				for (String param : params) {
					String[] keyValue = param.split("=");
					if (keyValue.length == 2) {
						if ("token".equals(keyValue[0])) {
							token = keyValue[1];
						} else if ("roomId".equals(keyValue[0])) {
							try {
								roomId = Long.parseLong(keyValue[1]);
							} catch (NumberFormatException e) {
								log.warn("Invalid roomId format: {}", keyValue[1]);
							}
						}
					}
				}
			}
			
			if (token == null || roomId == null) {
				log.warn("Missing token or roomId in query parameters");
				session.close(CloseStatus.NOT_ACCEPTABLE);
				return;
			}
		} else {
			// 헤더에서 토큰 추출
			token = authHeader.substring(7);
			try {
				roomId = Long.parseLong(roomIdHeader);
			} catch (NumberFormatException e) {
				log.warn("Invalid Room-Id header: {}", roomIdHeader);
				session.close(CloseStatus.NOT_ACCEPTABLE);
				return;
			}
		}

		Long userId;
		try {
			userId = jwtTokenProvider.getUserId(token);
		} catch (Exception e) {
			log.warn("Invalid JWT token: {}", e.getMessage());
			session.close(CloseStatus.NOT_ACCEPTABLE);
			return;
		}

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
			
			// Entity를 DTO로 변환하여 전송 (순환 참조 방지)
			ChatMessageDto responseDto = ChatMessageDto.fromEntity(savedMessage);
			
			// 발신자 제외하고 전송
			sendMessageToChatRoomExcludingSender(responseDto, chatRoomSessionIds, session.getId());

		} else if (chatMessageDto.getMessageType().equals(ChatMessage.MessageType.ENTER)) {
			// 시스템 메시지 (DB 저장 안함) - 간단한 DTO 생성
			ChatMessageDto enterDto = ChatMessageDto.builder()
				.content(chatMessageDto.getContent())
				.messageType(ChatMessage.MessageType.ENTER)
				.senderId(userId)
				.senderName("시스템")
				.chatRoomId(chatRoomId)
				.createdAt(LocalDateTime.now())
				.build();
			// 입장 메시지는 모든 사람에게 전송 (본인 포함)
			sendMessageToChatRoom(enterDto, chatRoomSessionIds);
		}

		if (chatRoomSessionIds != null && chatRoomSessionIds.size() >= 3) {
			removeClosedSession(chatRoomSessionIds);
		}
	}

	// 연결이 끊어진 세션 삭제
	private void removeClosedSession(Set<String> sessionIds) {
		sessionIds.removeIf(sessionId -> !sessionMap.containsKey(sessionId));
	}

	// 세션에게 메세지 전송 (DTO 버전)
	private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<String> sessionIds) {
		log.info("브로드캐스팅: 메시지 '{}' 를 {} 개 세션에게 전송", 
			chatMessageDto.getContent(), sessionIds != null ? sessionIds.size() : 0);
		
		if (sessionIds == null || sessionIds.isEmpty()) {
			log.warn("전송할 세션이 없습니다.");
			return;
		}
		
		sessionIds.parallelStream()
			.map(sessionId -> {
				ChatSession chatSession = sessionMap.get(sessionId);
				if (chatSession == null) {
					log.warn("세션 ID {}에 대한 ChatSession을 찾을 수 없습니다", sessionId);
				}
				return chatSession;
			})
			.filter(Objects::nonNull)
			.forEach(chatSession -> {
				log.info("유저 {}에게 메시지 전송 중...", chatSession.getUserId());
				sendMessage(chatSession.getSession(), chatMessageDto);
			});
	}

	// 발신자 제외하고 메시지 전송 (DTO 버전)
	private void sendMessageToChatRoomExcludingSender(ChatMessageDto chatMessageDto, Set<String> sessionIds, String senderSessionId) {
		log.info("브로드캐스팅 (발신자 제외): 메시지 '{}' 를 {} 개 세션에게 전송 (발신자 세션 {} 제외)", 
			chatMessageDto.getContent(), sessionIds != null ? sessionIds.size() - 1 : 0, senderSessionId);
		
		if (sessionIds == null || sessionIds.isEmpty()) {
			log.warn("전송할 세션이 없습니다.");
			return;
		}
		
		sessionIds.parallelStream()
			.filter(sessionId -> !sessionId.equals(senderSessionId)) // 발신자 세션 제외
			.map(sessionId -> {
				ChatSession chatSession = sessionMap.get(sessionId);
				if (chatSession == null) {
					log.warn("세션 ID {}에 대한 ChatSession을 찾을 수 없습니다", sessionId);
				}
				return chatSession;
			})
			.filter(Objects::nonNull)
			.forEach(chatSession -> {
				log.info("유저 {}에게 메시지 전송 중... (발신자 제외)", chatSession.getUserId());
				sendMessage(chatSession.getSession(), chatMessageDto);
			});
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			if (session.isOpen()) {
				String messageJson = mapper.writeValueAsString(message);
				log.info("세션 {}에 메시지 전송: {}", session.getId(), messageJson);
				session.sendMessage(new TextMessage(messageJson));
			} else {
				log.warn("세션 {}이 닫혀있어 메시지를 전송할 수 없습니다", session.getId());
			}
		} catch (IOException e) {
			log.error("메시지 전송 실패: {}", e.getMessage(), e);
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