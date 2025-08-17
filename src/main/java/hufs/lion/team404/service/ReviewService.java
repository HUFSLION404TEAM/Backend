package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.Review;
import hufs.lion.team404.domain.entity.User;
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
    
    public List<Review> findByMatching(Matching matching) {
        return reviewRepository.findByMatching(matching);
    }
    
    public List<Review> findByReviewerId(Long reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId);
    }
    
    public List<Review> findByRevieweeOrderByCreatedAtDesc(User reviewee) {
        // 동적으로 리뷰 대상자가 해당 유저인 리뷰들을 찾아야 함
        // 임시로 모든 리뷰를 가져와서 필터링 (성능상 문제가 있을 수 있음)
        return reviewRepository.findAll().stream()
                .filter(review -> {
                    User targetReviewee = review.getReviewee();
                    return targetReviewee != null && targetReviewee.getId().equals(reviewee.getId());
                })
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Review> findByReviewerType(Review.ReviewerType reviewerType) {
        return reviewRepository.findByReviewerType(reviewerType);
    }
    
    public boolean existsByMatchingAndReviewer(Matching matching, User reviewer) {
        return reviewRepository.existsByMatchingAndReviewer(matching, reviewer);
    }
    
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}
