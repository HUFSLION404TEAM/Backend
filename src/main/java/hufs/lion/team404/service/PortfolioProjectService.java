package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.PortfolioProject;
import hufs.lion.team404.repository.PortfolioProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioProjectService {
    
    private final PortfolioProjectRepository portfolioProjectRepository;
    
    @Transactional
    public PortfolioProject save(PortfolioProject portfolioProject) {
        return portfolioProjectRepository.save(portfolioProject);
    }
    
    public Optional<PortfolioProject> findById(Long id) {
        return portfolioProjectRepository.findById(id);
    }
    
    public List<PortfolioProject> findByPortfolioIdOrderByDisplayOrder(Long portfolioId) {
        return portfolioProjectRepository.findByPortfolioIdOrderByDisplayOrder(portfolioId);
    }
    
    public List<PortfolioProject> findByTitleContaining(String title) {
        return portfolioProjectRepository.findByTitleContaining(title);
    }
    
    public List<PortfolioProject> findAll() {
        return portfolioProjectRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        portfolioProjectRepository.deleteById(id);
    }
}
