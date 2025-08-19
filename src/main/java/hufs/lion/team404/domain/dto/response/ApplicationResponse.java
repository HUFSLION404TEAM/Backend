package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

	private Long id;
	private Long studentId;
	private String storeBusinessNumber;  // Long storeId → String storeBusinessNumber
	private String status;          // enum을 문자열로 내려줌 (원하면 Application.Status로 바꿔도 OK)

	private String title;

	private String projectName;
	private String outline;
	private String resolveProblem;
	private String necessity;

	private String jobDescription;
	private String result;
	private String requirements;

	private String expectedOutcome;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime submittedAt;
	private LocalDateTime deletedAt;

	// 첨부파일 정보
	private List<String> fileUrls;

	public static ApplicationResponse from(Application a) {
		return ApplicationResponse.builder()
			.id(a.getId())
			.studentId(a.getStudent() != null ? a.getStudent().getId() : null)
			.storeBusinessNumber(null) // Application에는 직접적인 Store 연관관계가 없음
			.status(a.getStatus() != null ? a.getStatus().name() : null)
			.title(a.getTitle())
			.projectName(a.getProjectName())
			.outline(a.getOutline())
			.resolveProblem(a.getResolveProblem())
			.necessity(a.getNecessity())
			.jobDescription(a.getJobDescription())
			.result(a.getResult())
			.requirements(a.getRequirements())
			.expectedOutcome(a.getExpectedOutcome())
			.createdAt(a.getCreatedAt())
			.updatedAt(a.getUpdatedAt())
			.submittedAt(a.getSubmittedAt())
			.deletedAt(a.getDeletedAt())
			.fileUrls(a.getFiles() != null ? 
				a.getFiles().stream()
					.map(file -> "/uploads/application/" + file.getSavedFileName())
					.collect(Collectors.toList()) : 
				List.of())
			.build();
	}
}
