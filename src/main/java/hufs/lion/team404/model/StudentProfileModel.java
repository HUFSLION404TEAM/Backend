package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.response.StudentProfileListResponse;
import hufs.lion.team404.domain.dto.response.StudentProfileResponse;
import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.domain.entity.Review;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.FavoriteService;
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
    private final FavoriteService favoriteService;
    
    /**
     * 전체 학생 프로필 목록 조회 (필터링 지원)
     */
    public List<StudentProfileListResponse> getAllStudentProfiles(
            String school, String major, String region, Boolean isEmployment, 
            String keyword, Double minTemperature, Double maxTemperature) {
        
        // 공개된 학생 프로필만 조회
        List<Student> allStudents = studentService.findAllPublicProfiles();
        
        // 필터링 적용
        List<Student> filteredStudents = allStudents.stream()
                .filter(student -> {
                    // 학교 필터
                    if (school != null && !school.isEmpty() && student.getSchool() != null) {
                        if (!student.getSchool().toLowerCase().contains(school.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    // 전공 필터
                    if (major != null && !major.isEmpty() && student.getMajor() != null) {
                        if (!student.getMajor().toLowerCase().contains(major.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    // 지역 필터
                    if (region != null && !region.isEmpty() && student.getRegion() != null) {
                        if (!student.getRegion().toLowerCase().contains(region.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    // 구직상태 필터
                    if (isEmployment != null) {
                        if (!isEmployment.equals(student.getIsEmployment())) {
                            return false;
                        }
                    }
                    
                    // 키워드 필터 (이름, 소개, 역량에서 검색)
                    if (keyword != null && !keyword.isEmpty()) {
                        String lowerKeyword = keyword.toLowerCase();
                        boolean found = false;
                        
                        // 이름에서 검색
                        if (student.getUser() != null && student.getUser().getName() != null) {
                            if (student.getUser().getName().toLowerCase().contains(lowerKeyword)) {
                                found = true;
                            }
                        }
                        
                        // 소개에서 검색
                        if (!found && student.getIntroduction() != null) {
                            if (student.getIntroduction().toLowerCase().contains(lowerKeyword)) {
                                found = true;
                            }
                        }
                        
                        // 역량에서 검색
                        if (!found && student.getCapabilities() != null) {
                            if (student.getCapabilities().toLowerCase().contains(lowerKeyword)) {
                                found = true;
                            }
                        }
                        
                        if (!found) {
                            return false;
                        }
                    }
                    
                    // 온도 필터
                    if (minTemperature != null && student.getTemperature() != null) {
                        if (student.getTemperature() < minTemperature) {
                            return false;
                        }
                    }
                    
                    if (maxTemperature != null && student.getTemperature() != null) {
                        if (student.getTemperature() > maxTemperature) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
        
        // 각 학생의 추가 정보 조회하여 응답 생성
        return filteredStudents.stream()
                .map(student -> {
                    // 포트폴리오 수 조회
                    Long portfolioCount = portfolioService.countByStudentAndIsPrivateFalse(student);
                    
                    // 찜 받은 수 조회 (해당 학생의 User ID로)
                    Long favoriteCount = student.getUser() != null ? 
                        favoriteService.countByTargetStudentUserId(student.getUser().getId()) : 0L;
                    
                    return StudentProfileListResponse.fromEntity(student, portfolioCount, favoriteCount);
                })
                // 온도 높은 순 → 최신 가입순으로 정렬
                .sorted((s1, s2) -> {
                    int tempCompare = Double.compare(s2.getTemperature() != null ? s2.getTemperature() : 0.0, 
                                                   s1.getTemperature() != null ? s1.getTemperature() : 0.0);
                    if (tempCompare != 0) {
                        return tempCompare;
                    }
                    return s2.getCreatedAt().compareTo(s1.getCreatedAt()); // 최신순
                })
                .collect(Collectors.toList());
    }
    
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
