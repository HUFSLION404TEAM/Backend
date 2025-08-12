package hufs.lion.team404.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	public Long createRecruiting(Long id, String title, String recruitmentPeriod, String progressPeriod, String price,
		String projectOutline, String expectedResults, String detailRequirement, List<MultipartFile> images) {

		if (images.size() > 3) {
			throw new CustomException(ErrorCode.IMAGE_COUNT_EXCEEDED);
		}

		Store store = storeService.findByUserId(id).orElseThrow(() -> new UserNotFoundException(id));

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

}
