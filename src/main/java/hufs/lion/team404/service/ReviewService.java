package hufs.lion.team404.service;

import hufs.lion.team404.entity.Review;
import hufs.lion.team404.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    
    @Transactional
    public Review save(Review review) {
        return reviewRepository.save(review);
    }
    
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }
    
    public List<Review> findByMatchingId(Long matchingId) {
        return reviewRepository.findByMatchingId(matchingId);
    }
    
    public List<Review> findByReviewerId(Long reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId);
    }
    
    public List<Review> findByRevieweeId(Long revieweeId) {
        return reviewRepository.findByRevieweeId(revieweeId);
    }
    
    public List<Review> findByReviewerType(Review.ReviewerType reviewerType) {
        return reviewRepository.findByReviewerType(reviewerType);
    }
    
    public Double getAverageRatingByRevieweeId(Long revieweeId) {
        return reviewRepository.getAverageRatingByRevieweeId(revieweeId);
    }
    
    public long countByRevieweeId(Long revieweeId) {
        return reviewRepository.countByRevieweeId(revieweeId);
    }
    
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}
