package hufs.lion.team404;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import hufs.lion.team404.domain.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class WebSocketTest {

    @LocalServerPort
    private int port;
    
    private WebSocketSession session;
    private CountDownLatch latch;
    private String receivedMessage;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(1);
        receivedMessage = null;
    }

    @Test
    void testWebSocketConnection() throws Exception {
        // Given
        String wsUrl = "ws://localhost:" + port + "/ws/chat";
        
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", "Bearer test-token"); // 실제 토큰으로 교체 필요
        headers.add("Room-Id", "1");
        
        WebSocketHandler handler = new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                log.info("연결 성공: {}", session.getId());
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
                receivedMessage = message.getPayload().toString();
                log.info("메시지 수신: {}", receivedMessage);
                latch.countDown();
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                log.error("전송 오류", exception);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
                log.info("연결 종료: {}", session.getId());
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };

        // When
        try {
            session = client.doHandshake(handler, headers, URI.create(wsUrl)).get();
            
            // 테스트 메시지 전송
            ChatMessage testMessage = new ChatMessage();
            testMessage.setMessageType(ChatMessage.MessageType.TEXT);
            testMessage.setContent("테스트 메시지입니다.");
            
            String messageJson = objectMapper.writeValueAsString(testMessage);
            session.sendMessage(new TextMessage(messageJson));
            
            // Then
            assertTrue(latch.await(5, TimeUnit.SECONDS), "메시지 응답을 받지 못했습니다.");
            assertNotNull(receivedMessage);
            
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    @Test
    void testInvalidTokenConnection() throws Exception {
        // Given
        String wsUrl = "ws://localhost:" + port + "/ws/chat";
        
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", "Bearer invalid-token");
        headers.add("Room-Id", "1");
        
        CountDownLatch connectionLatch = new CountDownLatch(1);
        boolean[] connectionFailed = {false};
        
        WebSocketHandler handler = new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                // 연결이 성공하면 안됨
                connectionLatch.countDown();
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {}

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                connectionFailed[0] = true;
                connectionLatch.countDown();
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
                connectionFailed[0] = true;
                connectionLatch.countDown();
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };

        // When & Then
        try {
            client.doHandshake(handler, headers, URI.create(wsUrl)).get();
            assertTrue(connectionLatch.await(5, TimeUnit.SECONDS), "연결 상태 변화를 기다리는 중 타임아웃");
            assertTrue(connectionFailed[0], "유효하지 않은 토큰으로 연결이 성공했습니다.");
        } catch (Exception e) {
            // 예외가 발생하는 것이 정상
            log.info("예상된 예외 발생: {}", e.getMessage());
        }
    }
}
