package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByMatchingId(Long matchingId);
    
    List<Review> findByReviewerId(Long reviewerId);
    
    List<Review> findByRevieweeId(Long revieweeId);
    
    List<Review> findByReviewerType(Review.ReviewerType reviewerType);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewee.id = :revieweeId")
    Double getAverageRatingByRevieweeId(Long revieweeId);
    
    long countByRevieweeId(Long revieweeId);
}
