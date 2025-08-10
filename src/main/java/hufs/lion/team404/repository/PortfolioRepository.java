package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByStudent_Id(Long studentId);

    List<Portfolio> findByIsPublic(Boolean isPublic);

    List<Portfolio> findByStudent_IdAndIsPublic(Long studentId, Boolean isPublic);

    List<Portfolio> findByTitleContaining(String title);

    List<Portfolio> findByRegion(String region);

    List<Portfolio> findByCareerContaining(String career);

    List<Portfolio> findByRegionAndCareerContaining(String region, String career);

    List<Portfolio> findByIsPublicTrueAndRegion(String region);

    List<Portfolio> findByIsPublicTrueAndCareerContaining(String career);

    List<Portfolio> findByIsPublicTrueAndRegionAndCareerContaining(String region, String career);

    Optional<Portfolio> findByIdAndStudent_User_Email(Long id, String email);

}