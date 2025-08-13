package hufs.lion.team404.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

	private final PortfolioRepository portfolioProjectRepository;

	@Transactional
	public Portfolio save(Portfolio portfolio) {
		return portfolioProjectRepository.save(portfolio);
	}

	public Optional<Portfolio> findById(Long id) {
		return portfolioProjectRepository.findById(id);
	}

	public List<Portfolio> findAll() {
		return portfolioProjectRepository.findAll();
	}

	@Transactional
	public void deleteById(Long id) {
		portfolioProjectRepository.deleteById(id);
	}
}
