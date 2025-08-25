package hufs.lion.team404.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hufs.lion.team404.domain.dto.response.RecruitingDetailResponse;
import hufs.lion.team404.domain.dto.response.RecruitingListResponse;
import hufs.lion.team404.domain.entity.Recruiting;
import hufs.lion.team404.domain.entity.RecruitingImage;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.exception.UserNotFoundException;
import hufs.lion.team404.service.FavoriteService;
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
	private final FavoriteService favoriteService; // 추가

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

	@Transactional(readOnly = true)
	public Recruiting getRecruitingById(Long id) {
		return recruitingService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("공고를 찾을 수 없습니다."));
	}

	/**
	 * 구인글 상세 조회
	 */
	public RecruitingDetailResponse getRecruitingDetail(Long recruitingId) {
		Recruiting recruiting = recruitingService.findById(recruitingId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND, "구인글을 찾을 수 없습니다."));

		// 해당 구인글의 좋아요 수 조회
		Long favoriteCount = favoriteService.countByTargetRecruitingId(recruitingId);

		return RecruitingDetailResponse.fromEntity(recruiting, favoriteCount);
	}

	/**
	 * 구인글 전체 목록 조회 (필터링 지원)
	 */
	public List<RecruitingListResponse> getAllRecruitings(String category, String keyword, Boolean isRecruiting) {
		List<Recruiting> recruitings = recruitingService.findAllWithFilters(category, keyword, isRecruiting);

		return recruitings.stream()
			.map(recruiting -> {
				Long favoriteCount = favoriteService.countByTargetRecruitingId(recruiting.getId());
				return RecruitingListResponse.fromEntity(recruiting, favoriteCount);
			})
			.collect(Collectors.toList());
	}

	/**
	 * 구인글 전체 목록 조회 (기본)
	 */
	public List<RecruitingListResponse> getAllRecruitings() {
		return getAllRecruitings(null, null, null);
	}

	/**
	 * 특정 업체의 구인글 목록 조회
	 */
	public List<RecruitingListResponse> getRecruitingsByStore(String businessNumber) {
		Store store = storeService.findByBusinessNumber(businessNumber)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND, "업체를 찾을 수 없습니다."));

		List<Recruiting> recruitings = recruitingService.findByStoreOrderByCreatedAtDesc(store);

		return recruitings.stream()
			.map(recruiting -> {
				Long favoriteCount = favoriteService.countByTargetRecruitingId(recruiting.getId());
				return RecruitingListResponse.fromEntity(recruiting, favoriteCount);
			})
			.collect(Collectors.toList());
	}

	/**
	 * 내 모든 업체의 구인글 목록 조회
	 */
	public List<RecruitingListResponse> getMyRecruitings(Long userId) {
		List<Store> myStores = storeService.findByUserId(userId);

		return myStores.stream()
			.flatMap(store -> recruitingService.findByStoreOrderByCreatedAtDesc(store).stream())
			.map(recruiting -> {
				Long favoriteCount = favoriteService.countByTargetRecruitingId(recruiting.getId());
				return RecruitingListResponse.fromEntity(recruiting, favoriteCount);
			})
			.sorted((r1, r2) -> Long.compare(r2.getId(), r1.getId())) // 최신순 정렬
			.collect(Collectors.toList());
	}

	/**
	 * 구인글 삭제 (권한 확인)
	 */
	public void deleteRecruiting(Long recruitingId, Long userId) {
		Recruiting recruiting = recruitingService.findById(recruitingId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND, "구인글을 찾을 수 없습니다."));

		// 구인글의 업체 소유자가 현재 사용자인지 확인
		if (!recruiting.getStore().getUser().getId().equals(userId)) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "구인글을 삭제할 권한이 없습니다.");
		}

		recruitingService.deleteById(recruitingId);
	}

}
