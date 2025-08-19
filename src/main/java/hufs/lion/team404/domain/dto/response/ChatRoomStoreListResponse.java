package hufs.lion.team404.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomStoreListResponse {
    private Long roomId;
    private String studentName;
    private String storeName;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
}
