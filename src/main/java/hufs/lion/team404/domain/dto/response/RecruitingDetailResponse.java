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

	// 업체 정보
	private StoreInfo store;

	@Data
	@Builder
	public static class StoreInfo {
		private String businessNumber;
		private String storeName;
		private String address;
		private String contact;
		private String category;
		private String introduction;
		private Double temperature;
		private String userName; // User의 이름
		private String userEmail; // User의 이메일
	}

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
			
			// 업체 정보
			.store(recruiting.getStore() != null ?
				StoreInfo.builder()
					.businessNumber(recruiting.getStore().getBusinessNumber())
					.storeName(recruiting.getStore().getStoreName())
					.address(recruiting.getStore().getAddress())
					.contact(recruiting.getStore().getContact())
					.category(recruiting.getStore().getCategory())
					.introduction(recruiting.getStore().getIntroduction())
					.temperature(recruiting.getStore().getTemperature())
					.userName(recruiting.getStore().getUser() != null ? recruiting.getStore().getUser().getName() : null)
					.userEmail(recruiting.getStore().getUser() != null ? recruiting.getStore().getUser().getEmail() : null)
					.build()
				: null)
			.build();
	}
}
