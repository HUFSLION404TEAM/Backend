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
    
    public List<Review> findByMatching(Matching matching) {
        return reviewRepository.findByMatching(matching);
    }
    
    public List<Review> findByRevieweeOrderByCreatedAtDesc(User reviewee) {
        // 동적으로 리뷰 대상자가 해당 유저인 리뷰들을 찾아야 함
        return reviewRepository.findAll().stream()
                .filter(review -> {
                    User targetReviewee = review.getReviewee();
                    return targetReviewee != null && targetReviewee.getId().equals(reviewee.getId());
                })
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public boolean existsByMatchingAndReviewer(Matching matching, User reviewer) {
        return reviewRepository.existsByMatchingAndReviewer(matching, reviewer);
    }
}
