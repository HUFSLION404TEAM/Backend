package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.response.StoreProfileResponse;
import hufs.lion.team404.domain.entity.Recruiting;
import hufs.lion.team404.domain.entity.Review;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.RecruitingService;
import hufs.lion.team404.service.ReviewService;
import hufs.lion.team404.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreProfileModel {
    
    private final StoreService storeService;
    private final RecruitingService recruitingService;
    private final ReviewService reviewService;
    
    /**
     * 가게 프로필 조회 (학생에서 가게 정보 보기)
     */
    public StoreProfileResponse getStoreProfile(String businessNumber) {
        // 1. 가게 정보 조회
        Store store = storeService.findById(businessNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        
        // 2. 공고 목록 조회
        List<Recruiting> recruitings = recruitingService.findByStoreOrderByCreatedAtDesc(store);
        List<StoreProfileResponse.RecruitingInfo> recruitingInfos = recruitings.stream()
                .map(this::convertToRecruitingInfo)
                .collect(Collectors.toList());
        
        // 3. 리뷰 목록 조회 (해당 가게가 받은 리뷰)
        List<Review> reviews = reviewService.findByRevieweeOrderByCreatedAtDesc(store.getUser());
        List<StoreProfileResponse.ReviewInfo> reviewInfos = reviews.stream()
                .map(this::convertToReviewInfo)
                .collect(Collectors.toList());
        
        // 4. 평균 별점 및 총 리뷰 수 계산
        Double averageRating = reviews.isEmpty() ? 0.0 : 
                reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        Integer totalReviews = reviews.size();
        
        // 5. StoreProfileResponse 생성
        return StoreProfileResponse.from(store, recruitingInfos, reviewInfos, averageRating, totalReviews);
    }
    
    /**
     * Recruiting을 RecruitingInfo로 변환
     */
    private StoreProfileResponse.RecruitingInfo convertToRecruitingInfo(Recruiting recruiting) {
        // 공고 이미지 URL 생성
        List<String> imageUrls = new ArrayList<>();
        if (recruiting.getImages() != null && !recruiting.getImages().isEmpty()) {
            imageUrls = recruiting.getImages().stream()
                    .filter(image -> image != null && image.getSavedFileName() != null)
                    .map(image -> "/uploads/recruiting/" + image.getSavedFileName())
                    .collect(Collectors.toList());
        }
        
        // 모집 중 여부 판단 (모집 기간 파싱)
        Boolean isRecruiting = isRecruitmentActive(recruiting.getRecruitmentPeriod());
        
        // price를 Integer로 변환 (String에서 숫자 추출)
        Integer priceInt = null;
        if (recruiting.getPrice() != null) {
            try {
                // 숫자가 아닌 문자들을 제거하고 파싱
                String cleanPrice = recruiting.getPrice().replaceAll("[^0-9]", "");
                if (!cleanPrice.isEmpty()) {
                    priceInt = Integer.parseInt(cleanPrice);
                }
            } catch (NumberFormatException e) {
                // 파싱 실패 시 null로 유지
            }
        }
        
        return StoreProfileResponse.RecruitingInfo.builder()
                .id(recruiting.getId())
                .title(recruiting.getTitle())
                .projectOverview(recruiting.getProjectOutline()) // projectOutline 사용
                .recruitmentPeriod(recruiting.getRecruitmentPeriod())
                .progressPeriod(recruiting.getProgressPeriod())
                .price(priceInt)
                .isRecruiting(isRecruiting)
                .imageUrls(imageUrls)
                .createdAt(recruiting.getCreatedAt())
                .build();
    }
    
    /**
     * Review를 ReviewInfo로 변환
     */
    private StoreProfileResponse.ReviewInfo convertToReviewInfo(Review review) {
        // 리뷰 작성자 정보 추출 (학생 이름)
        String reviewerName = review.getReviewer() != null && review.getReviewer().getName() != null 
                ? review.getReviewer().getName() : "알 수 없음";
        
        return StoreProfileResponse.ReviewInfo.builder()
                .id(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .reviewerName(reviewerName)
                .createdAt(review.getCreatedAt())
                .build();
    }
    
    /**
     * 모집 기간을 파싱하여 현재 모집 중인지 판단
     * 예: "2025-08-01 ~ 2025-08-31" 형태의 문자열을 파싱
     */
    private Boolean isRecruitmentActive(String recruitmentPeriod) {
        if (recruitmentPeriod == null || recruitmentPeriod.trim().isEmpty()) {
            return false;
        }
        
        try {
            // "YYYY-MM-DD ~ YYYY-MM-DD" 형태 파싱
            String[] dates = recruitmentPeriod.split("~");
            if (dates.length != 2) {
                return false;
            }
            
            LocalDate startDate = LocalDate.parse(dates[0].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate endDate = LocalDate.parse(dates[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate today = LocalDate.now();
            
            return !today.isBefore(startDate) && !today.isAfter(endDate);
        } catch (DateTimeParseException e) {
            // 파싱 실패 시 false 반환
            return false;
        }
    }
}
