package hufs.lion.team404.service;

import java.util.List;
import java.util.Optional;

import hufs.lion.team404.domain.dto.response.PortfolioUpdateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

	private final PortfolioRepository portfolioRepository;
	private final PortfolioImageService portfolioImageService;

	@Transactional
	public Portfolio save(Portfolio portfolio) {
		return portfolioRepository.save(portfolio);
	}

	public Optional<Portfolio> findById(Long id) {
		return portfolioRepository.findById(id);
	}

	public List<Portfolio> findAll() {
		return portfolioRepository.findAll();
	}

	// 수정 추가
	@Transactional
	public void update(Long portfolioId, String title, String progressPeriod, Boolean prize, String workDoneProgress,
					   String result, String felt, Boolean isPrivate, List<MultipartFile> images, String email) {

		Portfolio p = portfolioRepository.findById(portfolioId)
				.orElseThrow(() -> new IllegalArgumentException("포트폴리오를 찾을 수 없습니다."));

		if (!p.getStudent().getUser().getEmail().equals(email)) {
			throw new IllegalArgumentException("본인 포트폴리오만 수정할 수 있습니다.");
		}

		p.setTitle(title);
		p.setProgressPeriod(progressPeriod);
		p.setPrize(prize);
		p.setWorkDoneProgress(workDoneProgress);
		p.setResult(result);
		p.setFelt(felt);
		p.setPrivate(isPrivate);
		portfolioImageService.update(p, images);
	}


	@Transactional
	public void deleteById(Long id) {
		portfolioRepository.deleteById(id);
	}
}
