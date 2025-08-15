package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Application;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponse {
	private Long id;
	private Long studentId;
	private Long storeId;
	private Application.Status status; // 내부 enum 사용
	private String title;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ApplicationResponse from(Application a) {
		return ApplicationResponse.builder()
			.id(a.getId())
			.studentId(a.getStudentId())
			.storeId(a.getStoreId())
			.status(a.getStatus())
			.title(a.getTitle())
			.createdAt(a.getCreatedAt())
			.updatedAt(a.getUpdatedAt())
			.build();
	}
}
