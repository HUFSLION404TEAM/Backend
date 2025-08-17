package hufs.lion.team404.domain.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import hufs.lion.team404.domain.entity.Recruiting;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruitingDetailResponse {

	private Long id;
	private String title;
	private String recruitmentPeriod;
	private String progressPeriod;
	private String price;
	private String projectOutline;
	private String expectedResults;
	private String detailRequirement;

	// 이미지 URL 목록
	private List<String> imageUrls;

	public static RecruitingDetailResponse fromEntity(Recruiting recruiting) {
		return RecruitingDetailResponse.builder()
			.id(recruiting.getId())
			.title(recruiting.getTitle())
			.recruitmentPeriod(recruiting.getRecruitmentPeriod())
			.progressPeriod(recruiting.getProgressPeriod())
			.price(recruiting.getPrice())
			.projectOutline(recruiting.getProjectOutline())
			.expectedResults(recruiting.getExpectedResults())
			.detailRequirement(recruiting.getDetailRequirement())

			// 이미지 URL 목록 (RecruitingImage -> URL 변환)
			.imageUrls(recruiting.getImages() != null ?
				recruiting.getImages().stream()
					.map(image -> "/uploads/" + image.getSavedFileName()) // 저장된 파일명을 URL로 변환
					.collect(Collectors.toList())
				: List.of())
			.build();
	}
}
