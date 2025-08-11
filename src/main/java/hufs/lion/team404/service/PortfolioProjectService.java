package hufs.lion.team404.service;

import hufs.lion.team404.domain.dto.request.PortfolioProjectUpdateRequestDto;
import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.domain.entity.PortfolioProject;
import hufs.lion.team404.repository.PortfolioProjectRepository;
import hufs.lion.team404.repository.PortfolioRepository;
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
    private final PortfolioRepository portfolioRepository;
    
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
    public void update(Long portfolioId, Long id, PortfolioProjectUpdateRequestDto dto) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("포트폴리오를 찾을 수 없습니다."));

        PortfolioProject project = portfolioProjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        project.setTitle(dto.getTitle());
        project.setIntroduction(dto.getIntroduction());
        project.setOutline(dto.getOutline());
        project.setWork(dto.getWork());
        project.setProcess(dto.getProcess());
        project.setResult(dto.getResult());
        project.setGrow(dto.getGrow());
        project.setCompetency(dto.getCompetency());
        project.setPrize(dto.getPrize());
        project.setDisplayOrder(dto.getDisplayOrder());
    }

    
    @Transactional
    public void deleteById(Long id) {
        portfolioProjectRepository.deleteById(id);
    }
}
