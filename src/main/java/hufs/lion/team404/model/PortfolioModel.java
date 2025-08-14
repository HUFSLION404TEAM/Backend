package hufs.lion.team404.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.response.PortfolioResponse;
import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.domain.entity.PortfolioImage;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.exception.StudentNotFoundException;
import hufs.lion.team404.service.PortfolioImageService;
import hufs.lion.team404.service.PortfolioService;
import hufs.lion.team404.service.UserService;
import hufs.lion.team404.util.FileStorageUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortfolioModel {
	private final PortfolioService portfolioService;
	private final PortfolioImageService portfolioImageService;
	private final UserService userService;
	private final FileStorageUtil fileStorageUtil;

	// 생성
	public Long createPortfolio(String title, String progressPeriod, Boolean prize, String workDoneProgress,
		String result,
		String felt, Boolean isPrivate, List<MultipartFile> images, String email) {

		User user = userService.findByEmail(email)
			.orElseThrow(() -> new NotFoundException("User not found"));
		Student student = user.getStudent();
		if (student == null) {
			throw new StudentNotFoundException(user.getId() + "에 해당하는 학생이 없습니다.");
		}

		boolean prizeVal = Boolean.TRUE.equals(prize);
		boolean privateVal = Boolean.TRUE.equals(isPrivate);

		Portfolio portfolio = Portfolio.builder()
			.student(student)
			.title(title)
			.progressPeriod(progressPeriod)
			.prize(prizeVal)
			.workDoneProgress(workDoneProgress)
			.result(result)
			.felt(felt)
			.isPrivate(privateVal)
			.build();

		portfolioService.save(portfolio);

		if (!images.isEmpty()) {
			for (int i = 0; i < images.size(); i++) {
				MultipartFile image = images.get(i);
				if (!image.isEmpty()) {
					String savedFileName = fileStorageUtil.saveFile(image);

					PortfolioImage portfolioImage = PortfolioImage.builder()
						.portfolio(portfolio)
						.imagePath("/uploads/portfolio/" + savedFileName)
						.originalFileName(image.getOriginalFilename())
						.savedFileName(savedFileName)
						.fileSize(image.getSize())
						.contentType(image.getContentType())
						.imageOrder(i + 1)
						.uploadedAt(LocalDateTime.now())
						.build();

					portfolioImageService.save(portfolioImage);
				}

			}
		}
		return portfolio.getId();
	}
//
//	// 전체 포폴 조회
//	@Transactional(readOnly = true)
//	public List<Portfolio> getAllPortfolios() {
//		portfolioService.findAll().stream().map(portfolio -> {
//			return PortfolioResponse.builder()
//				.career(portfolio.get)
//		})
//		return portfolioService.findAll();
//	}

	// 포트포리오 조회 (포폴 ID)
	@Transactional(readOnly = true)
	public Portfolio getPortfolioById(Long id) {
		return portfolioService.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("포트폴리오를 찾을 수 없습니다."));
	}

	@Transactional
	public Long updatePortfolio(Long portfolioId, String title, String progressPeriod, Boolean prize,
		String workDoneProgress, String result, String felt, Boolean isPrivate,
		List<MultipartFile> images, String email) {

		User user = userService.findByEmail(email)
			.orElseThrow(() -> new NotFoundException("User not found"));

		Portfolio portfolio = portfolioService.findById(portfolioId)
			.orElseThrow(() -> new IllegalArgumentException("포트폴리오를 찾을 수 없습니다."));

		if (!portfolio.getStudent().getId().equals(user.getStudent().getId())) {
			throw new IllegalArgumentException("본인의 포트폴리오만 수정할 수 있습니다.");
		}

		boolean prizeVal = Boolean.TRUE.equals(prize);
		boolean privateVal = Boolean.TRUE.equals(isPrivate);

		if (title != null)
			portfolio.setTitle(title);
		if (progressPeriod != null)
			portfolio.setProgressPeriod(progressPeriod);
		if (prize != null)
			portfolio.setPrize(prize);
		if (workDoneProgress != null)
			portfolio.setWorkDoneProgress(workDoneProgress);
		if (result != null)
			portfolio.setResult(result);
		if (felt != null)
			portfolio.setFelt(felt);
		if (isPrivate != null)
			portfolio.setPrivate(isPrivate);

		if (images != null && !images.isEmpty()) {
			portfolioImageService.update(portfolio, images);
		}

		return portfolio.getId();
	}

	//    // 조회 - 필터링 조회 (공개, 지역, 경력)
	//    @Transactional(readOnly = true)
	//    public List<PortfolioResponse> getPortfoliosFiltered(Boolean isPublic, String region, String career){
	//        boolean hasRegion = region != null && !region.isBlank();
	//        boolean hasCareer = career != null && !career.isBlank();
	//
	//        List<Portfolio> isPublicPortfolio =
	//                Boolean.TRUE.equals(isPublic) ? portfolioService.findByIsPublic(true) :
	//                Boolean.FALSE.equals(isPublic) ? portfolioService.findByIsPublic(false) :
	//                        portfolioService.findAll();
	//
	//        List<PortfolioResponse> result = new ArrayList<>();
	//
	//        for (Portfolio portfolio : isPublicPortfolio) {
	//            if (hasRegion && (portfolio.getRegion() == null || !portfolio.getRegion().equals(region))) continue;
	//            if (hasCareer && (portfolio.getCareer() == null || !portfolio.getCareer().contains(career))) continue;
	//
	//            result.add(PortfolioResponse.builder()
	//                    .id(portfolio.getId())
	//                    .title(portfolio.getTitle())
	//                    .region(portfolio.getRegion())
	//                    .representSentence(portfolio.getRepresentSentence())
	//                    .career(portfolio.getCareer())
	//                    .studentName(portfolio.getStudent().getUser().getName())
	//                    .isPublic(portfolio.getIsPublic())
	//                    .createdAt(portfolio.getCreatedAt())
	//                    .updatedAt(portfolio.getUpdatedAt())
	//                    .build());
	//        }
	//        return result;
	//    }

	public void deletePortfolio(Long portfolioId, String email) {
		User user = userService.findByEmail(email)
			.orElseThrow(() -> new NotFoundException("User not found"));

		Student student = user.getStudent();
		if (student == null) {
			throw new StudentNotFoundException(user.getId() + "에 해당하는 학생이 없습니다.");
		}

		Portfolio portfolio = portfolioService.findById(portfolioId)
			.orElseThrow(() -> new EntityNotFoundException("포트폴리오를 찾을 수 없습니다."));

		if (!portfolio.getStudent().getId().equals(student.getId())) {
			throw new IllegalArgumentException("본인의 포트폴리오만 삭제할 수 있습니다.");
		}

		portfolioService.deleteById(portfolioId);
	}

	//  검색
	//    @Transactional(readOnly = true)
	//    public List<PortfolioResponse> searchAndFilterPublic(String region, String career, Boolean isJobSeeking, String q) {
	//
	//        List<Portfolio> list = portfolioService.searchAndFilterPublic(region, career, isJobSeeking, q);
	//
	//        return list.stream()
	//                .map(p -> PortfolioResponse.builder()
	//                        .id(p.getId())
	//                        .title(p.getTitle())
	//                        .region(p.getRegion())
	//                        .representSentence(p.getRepresentSentence())
	//                        .career(p.getCareer())
	//                        .studentName(p.getStudent().getUser().getName())
	//                        .isPublic(p.getIsPublic())
	//                        .isJobSeeking(p.getIsJobSeeking())
	//                        .createdAt(p.getCreatedAt())
	//                        .updatedAt(p.getUpdatedAt())
	//                        .build())
	//                .toList();
	//    }
}
