package hufs.lion.team404.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hufs.lion.team404.domain.dto.response.RecruitingDetailResponse;
import hufs.lion.team404.domain.dto.response.RecruitingListResponse;
import hufs.lion.team404.domain.entity.Recruiting;
import hufs.lion.team404.domain.entity.RecruitingImage;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.exception.UserNotFoundException;
import hufs.lion.team404.service.RecruitingImageService;
import hufs.lion.team404.service.RecruitingService;
import hufs.lion.team404.service.StoreService;
import hufs.lion.team404.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitingModel {
	private final RecruitingService recruitingService;
	private final StoreService storeService;
	private final FileStorageUtil fileStorageUtil;
	private final RecruitingImageService recruitingImageService;

	public Long createRecruiting(Long userId, String businessNumber, String title, String recruitmentPeriod, String progressPeriod, String price,
		String projectOutline, String expectedResults, String detailRequirement, List<MultipartFile> images) {

		if (images.size() > 3) {
			throw new CustomException(ErrorCode.IMAGE_COUNT_EXCEEDED);
		}

		// 사업자번호로 Store 조회
		Store store = storeService.findByBusinessNumber(businessNumber)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND, "업체를 찾을 수 없습니다."));

		// 해당 Store의 소유자가 현재 사용자인지 확인
		if (!store.getUser().getId().equals(userId)) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "해당 업체의 소유자가 아닙니다.");
		}

		Recruiting recruiting = Recruiting.builder()
			.title(title)
			.recruitmentPeriod(recruitmentPeriod)
			.progressPeriod(progressPeriod)
			.price(price)
			.projectOutline(projectOutline)
			.expectedResults(expectedResults)
			.detailRequirement(detailRequirement)
			.store(store)
			.build();

		recruitingService.save(recruiting);

		if (!images.isEmpty()) {
			for (int i = 0; i < images.size(); i++) {
				MultipartFile image = images.get(i);
				if (!image.isEmpty()) {
					String savedFileName = fileStorageUtil.saveFile(image);

					RecruitingImage recruitingImage = RecruitingImage.builder()
						.recruiting(recruiting)
						.imagePath("/uploads/recruiting/" + savedFileName)
						.originalFileName(image.getOriginalFilename())
						.savedFileName(savedFileName)
						.fileSize(image.getSize())
						.contentType(image.getContentType())
						.imageOrder(i + 1)
						.uploadedAt(LocalDateTime.now())
						.build();

					recruitingImageService.save(recruitingImage);
				}
			}
		}
		return recruiting.getId();
	}

	/**
	 * 구인글 상세 조회
	 */
	public RecruitingDetailResponse getRecruitingDetail(Long recruitingId) {
		Recruiting recruiting = recruitingService.findById(recruitingId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND, "구인글을 찾을 수 없습니다."));

		return RecruitingDetailResponse.fromEntity(recruiting);
	}

	/**
	 * 구인글 전체 목록 조회
	 */
	public List<RecruitingListResponse> getAllRecruitings() {
		List<Recruiting> recruitings = recruitingService.findAll();
		
		return recruitings.stream()
			.map(RecruitingListResponse::fromEntity)
			.collect(Collectors.toList());
	}

}
