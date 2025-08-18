package hufs.lion.team404.domain.dto.response;

import java.time.LocalDateTime;

import hufs.lion.team404.domain.entity.Matching;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchingResponse {

	private Long id;
	private String status;
	private String matchedBy;
	private LocalDateTime offeredAt;
	private LocalDateTime respondedAt;
	private LocalDateTime createdAt;

	// 가게 정보
	private String businessNumber;
	private String storeName;
	private String storeEmail;

	private Long studentId;
	private String studentName;
	private String studentEmail;

	// 채팅방 정보
	private Long chatRoomId;

	public static MatchingResponse fromEntity(Matching matching) {
		return MatchingResponse.builder()
			.id(matching.getId())
			.status(matching.getStatus().name())
			.matchedBy(matching.getMatchedBy().name())
			.offeredAt(matching.getOfferedAt())
			.respondedAt(matching.getRespondedAt())
			.createdAt(matching.getCreatedAt())

			// 가게 정보
			.businessNumber(matching.getProjectRequest().getStore().getBusinessNumber())
			.storeName(matching.getProjectRequest().getStore().getStoreName())
			.storeEmail(matching.getProjectRequest().getStore().getUser().getEmail())

			// 학생 정보
			.studentId(matching.getChatRoom() != null ? matching.getChatRoom().getStudent().getId() : null)
			.studentName(
				matching.getChatRoom() != null ? matching.getChatRoom().getStudent().getUser().getName() : null)
			.studentEmail(
				matching.getChatRoom() != null ? matching.getChatRoom().getStudent().getUser().getEmail() : null)
			.chatRoomId(matching.getChatRoom() != null ? matching.getChatRoom().getId() : null)
			.build();
	}
}
