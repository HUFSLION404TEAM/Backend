package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.response.StudentProfileResponse;
import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.domain.entity.Review;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.PortfolioService;
import hufs.lion.team404.service.ReviewService;
import hufs.lion.team404.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentProfileModel {
    
    private final StudentService studentService;
    private final PortfolioService portfolioService;
    private final ReviewService reviewService;
    
    /**
     * 학생 프로필 조회 (가게에서 학생 정보 보기)
     */
    public StudentProfileResponse getStudentProfile(Long studentId) {
        // 1. 학생 정보 조회 (공개된 프로필만)
        Student student = studentService.findPublicProfileById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));
        
        // 2. 포트폴리오 목록 조회 (공개된 포트폴리오만)
        List<Portfolio> portfolios = portfolioService.findByStudentAndIsPrivateFalse(student);
        List<StudentProfileResponse.PortfolioInfo> portfolioInfos = portfolios.stream()
                .map(this::convertToPortfolioInfo)
                .collect(Collectors.toList());
        
        // 3. 리뷰 목록 조회 (해당 학생이 받은 리뷰)
        List<Review> reviews = reviewService.findByRevieweeOrderByCreatedAtDesc(student.getUser());
        List<StudentProfileResponse.ReviewInfo> reviewInfos = reviews.stream()
                .map(this::convertToReviewInfo)
                .collect(Collectors.toList());
        
        // 4. 평균 별점 및 총 리뷰 수 계산
        Double averageRating = reviews.isEmpty() ? 0.0 : 
                reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        Integer totalReviews = reviews.size();
        
        // 5. StudentProfileResponse 생성
        return StudentProfileResponse.from(student, portfolioInfos, reviewInfos, averageRating, totalReviews);
    }
    
    /**
     * Portfolio를 PortfolioInfo로 변환
     */
    private StudentProfileResponse.PortfolioInfo convertToPortfolioInfo(Portfolio portfolio) {
        // 포트폴리오 이미지 URL 생성
        List<String> imageUrls = new ArrayList<>();
        if (portfolio.getImages() != null && !portfolio.getImages().isEmpty()) {
            imageUrls = portfolio.getImages().stream()
                    .filter(image -> image != null && image.getSavedFileName() != null)
                    .map(image -> "/uploads/portfolio/" + image.getSavedFileName())
                    .collect(Collectors.toList());
        }
        
        return StudentProfileResponse.PortfolioInfo.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .projectName(portfolio.getProjectName())
                .outline(portfolio.getOutline())
                .imageUrls(imageUrls)
                .createdAt(portfolio.getCreatedAt())
                .build();
    }
    
    /**
     * Review를 ReviewInfo로 변환
     */
    private StudentProfileResponse.ReviewInfo convertToReviewInfo(Review review) {
        // 리뷰 작성자 정보 추출
        String reviewerName = review.getReviewer() != null && review.getReviewer().getName() != null 
                ? review.getReviewer().getName() : "알 수 없음";
        
        // 업체명 추출 (매칭을 통해 업체 정보 가져오기)
        String storeName = "";
        if (review.getMatching() != null && review.getMatching().getChatRoom() != null 
                && review.getMatching().getChatRoom().getStore() != null
                && review.getMatching().getChatRoom().getStore().getStoreName() != null) {
            storeName = review.getMatching().getChatRoom().getStore().getStoreName();
        }
        
        return StudentProfileResponse.ReviewInfo.builder()
                .id(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewerName(reviewerName)
                .storeName(storeName)
                .createdAt(review.getCreatedAt())
                .build();
    }
}
