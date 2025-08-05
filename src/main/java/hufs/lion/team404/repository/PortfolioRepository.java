package hufs.lion.team404.repository;

import hufs.lion.team404.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    List<Portfolio> findByStudentId(Long studentId);
    
    List<Portfolio> findByIsPublic(Boolean isPublic);
    
    List<Portfolio> findByStudentIdAndIsPublic(Long studentId, Boolean isPublic);
    
    List<Portfolio> findByTitleContaining(String title);
}
