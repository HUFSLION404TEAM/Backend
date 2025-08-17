package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.request.ReviewCreateRequestDto;
import hufs.lion.team404.domain.dto.response.ReviewResponse;
import hufs.lion.team404.domain.entity.*;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.MatchingService;
import hufs.lion.team404.service.ReviewService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewModel {
    
    private final ReviewService reviewService;
    private final UserService userService;
    private final MatchingService matchingService;
    
    /**
     * 리뷰 작성
     */
    @Transactional
    public Long createReview(ReviewCreateRequestDto request, String email) {
        log.info("Creating review for matching: {}, user email: {}", request.getMatchingId(), email);
        
        User reviewer = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        Matching matching = matchingService.findById(request.getMatchingId())
                .orElseThrow(() -> new CustomException(ErrorCode.MATCHING_NOT_FOUND));
        
        // 매칭이 완료되었는지 확인
        if (matching.getStatus() != Matching.Status.COMPLETED) {
            throw new CustomException(ErrorCode.INVALID_MATCHING_STATUS);
        }
        
        // 매칭 참여자인지 확인 및 리뷰 대상자 확인
        validateMatchingParticipant(matching, reviewer);
        
        // 이미 리뷰를 작성했는지 확인
        boolean alreadyReviewed = reviewService.existsByMatchingAndReviewer(matching, reviewer);
        if (alreadyReviewed) {
            throw new CustomException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }
        
        // 리뷰 생성
        Review review = new Review();
        review.setMatching(matching);
        review.setReviewer(reviewer);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        
        // 리뷰어 타입 설정
        if (reviewer.getStudent() != null) {
            review.setReviewerType(Review.ReviewerType.STUDENT);
        } else if (reviewer.getStore() != null) {
            review.setReviewerType(Review.ReviewerType.STORE);
        }
        
        Review savedReview = reviewService.save(review);
        
        // 리뷰 받은 사람의 온도 조정 (Student 또는 Store)
        User reviewee = review.getReviewee();
        if (reviewee != null) {
            if (reviewee.getStudent() != null) {
                // 학생의 온도 조정
                reviewee.getStudent().adjustTemperature(request.getRating());
                userService.save(reviewee);
            } else if (reviewee.getStore() != null) {
                // 가게의 온도 조정
                reviewee.getStore().adjustTemperature(request.getRating());
                userService.save(reviewee);
            }
        }
        
        log.info("Review created successfully. ID: {}, Rating: {}, Temperature adjusted for user: {}", 
                savedReview.getId(), request.getRating(), reviewee != null ? reviewee.getId() : null);
        
        return savedReview.getId();
    }
    
    /**
     * 매칭 참여자 확인
     */
    private void validateMatchingParticipant(Matching matching, User user) {
        User student = matching.getChatRoom().getStudent().getUser();
        User store = matching.getChatRoom().getStore().getUser();
        
        if (!user.getId().equals(student.getId()) && !user.getId().equals(store.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
    
    /**
     * 매칭별 리뷰 조회
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByMatching(Long matchingId, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        Matching matching = matchingService.findById(matchingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MATCHING_NOT_FOUND));
        
        // 매칭 참여자인지 확인
        User student = matching.getChatRoom().getStudent().getUser();
        User store = matching.getChatRoom().getStore().getUser();
        
        if (!user.getId().equals(student.getId()) && !user.getId().equals(store.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        
        List<Review> reviews = reviewService.findByMatching(matching);
        return reviews.stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자별 받은 리뷰 조회
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUser(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        List<Review> reviews = reviewService.findByRevieweeOrderByCreatedAtDesc(user);
        return reviews.stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
