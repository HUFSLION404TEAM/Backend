package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Student;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class StudentProfileResponse {
    
    // 기본 정보
    private Long id;
    private String name;
    private String email;
    private String school; // 대학교
    private String major; // 전공
    private String career; // 경력
    private String introduction; // 자기소개
    private String capabilities; // 보유 역량
    private String region;
    private Double temperature; // 온도
    private LocalDateTime createdAt;
    
    // 포트폴리오 정보
    private List<PortfolioInfo> portfolios;
    
    // 매칭 후기 (리뷰)
    private List<ReviewInfo> reviews;
    private Double averageRating; // 평균 별점
    private Integer totalReviews; // 총 리뷰 수
    
    @Data
    @NoArgsConstructor
    public static class PortfolioInfo {
        private Long id;
        private String title;
        private String projectName;
        private String outline;
        private List<String> imageUrls;
        private LocalDateTime createdAt;
        
        @Builder
        public PortfolioInfo(Long id, String title, String projectName, String outline, 
                            List<String> imageUrls, LocalDateTime createdAt) {
            this.id = id;
            this.title = title;
            this.projectName = projectName;
            this.outline = outline;
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
        private String reviewerName; // 리뷰 작성자 이름
        private String storeName; // 업체명
        private LocalDateTime createdAt;
        
        @Builder
        public ReviewInfo(Long id, Integer rating, String content, String reviewerName, 
                         String storeName, LocalDateTime createdAt) {
            this.id = id;
            this.rating = rating;
            this.content = content;
            this.reviewerName = reviewerName;
            this.storeName = storeName;
            this.createdAt = createdAt;
        }
    }
    
    @Builder
    public StudentProfileResponse(Long id, String name, String email, String school, String major,
                                 String career, String introduction, String capabilities, String region,
                                 Double temperature, LocalDateTime createdAt, List<PortfolioInfo> portfolios,
                                 List<ReviewInfo> reviews, Double averageRating, Integer totalReviews) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.school = school;
        this.major = major;
        this.career = career;
        this.introduction = introduction;
        this.capabilities = capabilities;
        this.region = region;
        this.temperature = temperature;
        this.createdAt = createdAt;
        this.portfolios = portfolios;
        this.reviews = reviews;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
    }
    
    /**
     * Student 엔티티에서 StudentProfileResponse로 변환
     */
    public static StudentProfileResponse from(Student student, List<PortfolioInfo> portfolios, 
                                            List<ReviewInfo> reviews, Double averageRating, Integer totalReviews) {
        return StudentProfileResponse.builder()
                .id(student.getId())
                .name(student.getUser().getName())
                .email(student.getUser().getEmail())
                .school(student.getSchool())
                .major(student.getMajor())
                .career(student.getCareer())
                .introduction(student.getIntroduction())
                .capabilities(student.getCapabilities())
                .region(student.getRegion())
                .temperature(student.getTemperature())
                .createdAt(student.getCreatedAt())
                .portfolios(portfolios)
                .reviews(reviews)
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .build();
    }
}
