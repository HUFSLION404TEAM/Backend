package hufs.lion.team404.domain.dto.response;

import java.time.LocalDateTime;

import hufs.lion.team404.domain.enums.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
	private Long id;
	private String name;
	private String email;
	private String profileImage;
	private String socialProvider;
	private String socialId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private UserRole userRole;
	private boolean hasStudent;
	private boolean hasStores;

	@Builder
	public UserResponse(Long id, String name, String email, String profileImage, String socialProvider, String socialId,
		LocalDateTime createdAt, LocalDateTime updatedAt, UserRole userRole, boolean hasStudent, boolean hasStores) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.profileImage = profileImage;
		this.socialProvider = socialProvider;
		this.socialId = socialId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.userRole = userRole;
		this.hasStudent = hasStudent;
		this.hasStores = hasStores;
	}
}
