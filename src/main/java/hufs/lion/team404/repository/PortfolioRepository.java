package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @EntityGraph(attributePaths = {"student.user"})
    @Query("""
        SELECT p FROM Portfolio p
        WHERE p.isPublic = true
          AND (:region IS NULL OR :region = '' OR p.region = :region)                     
          AND (:career IS NULL OR :career = '' OR p.career LIKE CONCAT('%', :career, '%')) 
          AND (:isJobSeeking IS NULL OR p.isJobSeeking = :isJobSeeking)                    
          AND (
               :q IS NULL OR :q = '' OR                                                
               LOWER(p.title)             LIKE LOWER(CONCAT('%', :q, '%')) OR
               LOWER(p.representSentence) LIKE LOWER(CONCAT('%', :q, '%')) OR
               LOWER(p.career)            LIKE LOWER(CONCAT('%', :q, '%'))
          )
        ORDER BY p.createdAt DESC
        """)
    List<Portfolio> searchAndFilterPublic(
            @Param("region") String region,
            @Param("career") String career,
            @Param("isJobSeeking") Boolean isJobSeeking,
            @Param("q") String q
    );
}