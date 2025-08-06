package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.PortfolioProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioProjectRepository extends JpaRepository<PortfolioProject, Long> {
    
    List<PortfolioProject> findByPortfolioIdOrderByDisplayOrder(Long portfolioId);
    
    List<PortfolioProject> findByTitleContaining(String title);
}
