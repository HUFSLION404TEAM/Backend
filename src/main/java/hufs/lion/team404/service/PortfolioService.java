package hufs.lion.team404.service;

import hufs.lion.team404.domain.dto.request.PortfolioUpdateRequestDto;
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


    // 조회
    public Optional<Portfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }
    
    public List<Portfolio> findByStudentId(Long studentId) {
        return portfolioRepository.findByStudent_Id(studentId);
    }
    
    public List<Portfolio> findByIsPublic(Boolean isPublic) {
        return portfolioRepository.findByIsPublic(isPublic);
    }

    public List<Portfolio> findByStudentIdAndIsPublic(Long studentId, Boolean isPublic) {
        return portfolioRepository.findByStudent_IdAndIsPublic(studentId, isPublic);
    }

    public List<Portfolio> findByTitleContaining(String title) {
        return portfolioRepository.findByTitleContaining(title);
    }
    
    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    public List<Portfolio> findByIsPublicTrueAndRegion(String region) {
        return portfolioRepository.findByIsPublicTrueAndRegion(region);
    }
    public List<Portfolio> findByIsPublicTrueAndCareerContaining(String career) {
        return portfolioRepository.findByIsPublicTrueAndCareerContaining(career);
    }
    public List<Portfolio> findByIsPublicTrueAndRegionAndCareerContaining(String region, String career) {
        return portfolioRepository.findByIsPublicTrueAndRegionAndCareerContaining(region, career);
    }
    public List<Portfolio> findByRegion(String region) {
        return portfolioRepository.findByRegion(region);
    }
    public List<Portfolio> findByCareerContaining(String career) {
        return portfolioRepository.findByCareerContaining(career);
    }
    public List<Portfolio> findByRegionAndCareerContaining(String region, String career) {
        return portfolioRepository.findByRegionAndCareerContaining(region, career);
    }

    @Transactional
    public void update(Long id, PortfolioUpdateRequestDto req, String email) {
        Portfolio p = portfolioRepository.findByIdAndStudent_User_Email(id, email)
                .orElseThrow(() -> new IllegalArgumentException("포트폴리오를 찾을 수 없습니다."));

        p.setTitle(req.getTitle());
        p.setRegion(req.getRegion());
        p.setRepresentSentence(req.getRepresentSentence());
        p.setCareer(req.getCareer());
        p.setIsPublic(req.getIsPublic());
    }


    @Transactional
    public void deleteById(Long id) {
        portfolioRepository.deleteById(id);
    }

}
