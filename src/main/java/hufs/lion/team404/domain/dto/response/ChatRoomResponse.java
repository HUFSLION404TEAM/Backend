package hufs.lion.team404.domain.dto.response;

import java.time.LocalDateTime;

import hufs.lion.team404.domain.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {

	private Long id;
	private LocalDateTime createdAt;
	private LocalDateTime lastMessageAt;
	private String lastMessage;

	private StudentInfo student;
	private StoreInfo store;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class StudentInfo {
		private Long id;
		private String name;
		private String email;
		private String major;
		private String profileImageUrl;
		private Double temperature;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class StoreInfo {
		private String businessNumber;
		private String storeName;
		private String category;
		private String address;
		private String contact;
		private Double temperature;
	}

	public static ChatRoomResponse from(ChatRoom chatRoom) {
		return ChatRoomResponse.builder()
			.id(chatRoom.getId())
			.createdAt(chatRoom.getCreatedAt())
			.lastMessageAt(chatRoom.getLastMessageAt())
			.student(chatRoom.getStudent() != null ? StudentInfo.builder()
				.id(chatRoom.getStudent().getId())
				.name(chatRoom.getStudent().getUser().getName())
				.email(chatRoom.getStudent().getUser().getEmail())
				.profileImageUrl(chatRoom.getStudent().getUser().getProfileImage())
				.temperature(chatRoom.getStudent().getTemperature())
				.build() : null)
			.store(chatRoom.getStore() != null ? StoreInfo.builder()
				.businessNumber(chatRoom.getStore().getBusinessNumber())
				.storeName(chatRoom.getStore().getStoreName())
				.category(chatRoom.getStore().getCategory())
				.address(chatRoom.getStore().getAddress())
				.contact(chatRoom.getStore().getContact())
				.temperature(chatRoom.getStore().getTemperature())
				.build() : null)
			.build();
	}
}
