package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {
    
    private final PortfolioRepository portfolioRepository;
    
    @Transactional
    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }
    
    public Optional<Portfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }
    
    public List<Portfolio> findByStudentId(Long studentId) {
        return portfolioRepository.findByStudentId(studentId);
    }
    
    public List<Portfolio> findByIsPublic(Boolean isPublic) {
        return portfolioRepository.findByIsPublic(isPublic);
    }
    
    public List<Portfolio> findByStudentIdAndIsPublic(Long studentId, Boolean isPublic) {
        return portfolioRepository.findByStudentIdAndIsPublic(studentId, isPublic);
    }
    
    public List<Portfolio> findByTitleContaining(String title) {
        return portfolioRepository.findByTitleContaining(title);
    }
    
    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        portfolioRepository.deleteById(id);
    }
}
