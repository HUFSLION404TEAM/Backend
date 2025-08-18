package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentMyPageResponse {

	private UserInfo userInfo;
	private List<MatchingHistoryInfo> matchingHistory;
	private StudentInfo studentInfo;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserInfo {
		private Long id;
		private String name;
		private String email;
		private String profileImageUrl;
		private LocalDateTime joinedAt;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MatchingHistoryInfo {
		private Long matchingId;
		private String studentName;
		private String projectTitle;
		private String status;
		private String period;
		private LocalDateTime matchedAt;
		private LocalDateTime completedAt;
		private boolean isCompleted;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class StudentInfo {
		private Long id;
		private Boolean isAuthenticated;
		private String introduction;
		private String career;
		private String school;
		private String birth;
		private String phoneCall;
		private Boolean isPublic;
		private Boolean isEmployment;
		private String region;
		private Double temperature;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}
}
