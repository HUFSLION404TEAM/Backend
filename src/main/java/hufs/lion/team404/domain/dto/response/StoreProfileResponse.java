package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Store;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class StoreProfileResponse {
    
    // 기본 정보
    private String businessNumber; // 사업자번호 (ID)
    private String storeName; // 업체명
    private String ownerName; // 대표자명
    private String category; // 업종
    private String address; // 업체주소
    private String phoneNumber; // 연락처
    private String email; // 이메일
    private String introduction; // 업체 소개
    private Double temperature; // 온도
    private LocalDateTime createdAt;
    
    // 공고 정보
    private List<RecruitingInfo> recruitings;
    
    // 매칭 후기 (리뷰)
    private List<ReviewInfo> reviews;
    private Double averageRating; // 평균 별점
    private Integer totalReviews; // 총 리뷰 수
    
    @Data
    @NoArgsConstructor
    public static class RecruitingInfo {
        private Long id;
        private String title;
        private String projectOverview;
        private String recruitmentPeriod; // 모집 기간
        private String progressPeriod; // 진행 기간
        private Integer price; // 가격
        private Boolean isRecruiting; // 모집 중 여부
        private List<String> imageUrls;
        private LocalDateTime createdAt;
        
        @Builder
        public RecruitingInfo(Long id, String title, String projectOverview, String recruitmentPeriod,
                             String progressPeriod, Integer price, Boolean isRecruiting, 
                             List<String> imageUrls, LocalDateTime createdAt) {
            this.id = id;
            this.title = title;
            this.projectOverview = projectOverview;
            this.recruitmentPeriod = recruitmentPeriod;
            this.progressPeriod = progressPeriod;
            this.price = price;
            this.isRecruiting = isRecruiting;
            this.imageUrls = imageUrls;
            this.createdAt = createdAt;
        }
    }
    
    @Data
    @NoArgsConstructor
    public static class ReviewInfo {
        private Long id;
        private Integer rating;
        private String content;
        private String reviewerName; // 리뷰 작성자 이름 (학생)
        private LocalDateTime createdAt;
        
        @Builder
        public ReviewInfo(Long id, Integer rating, String content, String reviewerName, 
                         LocalDateTime createdAt) {
            this.id = id;
            this.rating = rating;
            this.content = content;
            this.reviewerName = reviewerName;
            this.createdAt = createdAt;
        }
    }
    
    @Builder
    public StoreProfileResponse(String businessNumber, String storeName, String ownerName, String category,
                               String address, String phoneNumber, String email, String introduction,
                               Double temperature, LocalDateTime createdAt, List<RecruitingInfo> recruitings,
                               List<ReviewInfo> reviews, Double averageRating, Integer totalReviews) {
        this.businessNumber = businessNumber;
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.category = category;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.introduction = introduction;
        this.temperature = temperature;
        this.createdAt = createdAt;
        this.recruitings = recruitings;
        this.reviews = reviews;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
    }
    
    /**
     * Store 엔티티에서 StoreProfileResponse로 변환
     */
    public static StoreProfileResponse from(Store store, List<RecruitingInfo> recruitings, 
                                          List<ReviewInfo> reviews, Double averageRating, Integer totalReviews) {
        return StoreProfileResponse.builder()
                .businessNumber(store.getBusinessNumber())
                .storeName(store.getStoreName())
                .ownerName(store.getUser().getName()) // User의 이름이 대표자명
                .category(store.getCategory())
                .address(store.getAddress())
                .phoneNumber(store.getContact())
                .email(store.getUser().getEmail())
                .introduction(store.getIntroduction())
                .temperature(store.getTemperature())
                .createdAt(store.getCreatedAt())
                .recruitings(recruitings)
                .reviews(reviews)
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .build();
    }
}
