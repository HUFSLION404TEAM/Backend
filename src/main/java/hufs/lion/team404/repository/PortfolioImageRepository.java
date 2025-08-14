package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.PortfolioImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioImageRepository extends JpaRepository<PortfolioImage, Long> {
    List<PortfolioImage> findByPortfolioIdOrderByImageOrderAsc(Long portfolioId);
}
