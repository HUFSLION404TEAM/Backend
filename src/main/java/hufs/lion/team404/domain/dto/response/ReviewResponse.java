package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Review;
import hufs.lion.team404.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리뷰 응답 DTO")
public class ReviewResponse {
    
    @Schema(description = "리뷰 ID", example = "1")
    private Long id;
    
    @Schema(description = "매칭 ID", example = "1")
    private Long matchingId;
    
    @Schema(description = "리뷰어 ID", example = "1")
    private Long reviewerId;
    
    @Schema(description = "리뷰어 이름", example = "김학생")
    private String reviewerName;
    
    @Schema(description = "리뷰 대상자 ID", example = "2")
    private Long revieweeId;
    
    @Schema(description = "리뷰 대상자 이름", example = "박사장")
    private String revieweeName;

    @Schema(description = "리뷰 대상자 온도", example = "36.8")
    private Double revieweeTemperature;
    
    @Schema(description = "리뷰어 타입", example = "STUDENT")
    private Review.ReviewerType reviewerType;
    
    @Schema(description = "별점 (0~5)", example = "4")
    private Integer rating;
    
    @Schema(description = "리뷰 내용", example = "정말 만족스러운 작업이었습니다!")
    private String content;
    
    @Schema(description = "작성 시간", example = "2024-08-17T10:00:00")
    private LocalDateTime createdAt;
    
    public static ReviewResponse fromEntity(Review review) {
        Double revieweeTemperature = null;
        User reviewee = review.getReviewee();
        
        if (reviewee != null) {
            if (reviewee.getStudent() != null) {
                // 리뷰 대상자가 학생인 경우
                revieweeTemperature = reviewee.getStudent().getTemperature();
            } else {
                // 리뷰 대상자가 업체 소유자인 경우, 매칭에서 해당 업체를 찾아야 함
                if (review.getMatching() != null && review.getMatching().getChatRoom() != null) {
                    // 매칭의 채팅방에서 해당 Store 가져오기
                    revieweeTemperature = review.getMatching().getChatRoom().getStore().getTemperature();
                }
            }
        }
        
        return new ReviewResponse(
                review.getId(),
                review.getMatching().getId(),
                review.getReviewer().getId(),
                review.getReviewer().getName(),
                reviewee != null ? reviewee.getId() : null,
                reviewee != null ? reviewee.getName() : "알 수 없음",
                revieweeTemperature,
                review.getReviewerType(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
