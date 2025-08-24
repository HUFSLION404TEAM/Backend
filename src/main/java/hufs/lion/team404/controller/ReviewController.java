package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.ReviewCreateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.ReviewResponse;
import hufs.lion.team404.model.ReviewModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "리뷰", description = "리뷰 작성 및 조회 관련 API")
public class ReviewController {
    
    private final ReviewModel reviewModel;
    
    @PostMapping
    @Operation(
            summary = "리뷰 작성",
            description = "완료된 매칭에 대해 리뷰를 작성합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Long> createReview(
            @Valid @RequestBody ReviewCreateRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long reviewId = reviewModel.createReview(request, userPrincipal.getEmail());
        return ApiResponse.success("리뷰가 성공적으로 작성되었습니다.", reviewId);
    }
    
    @GetMapping("/matching/{matchingId}")
    @Operation(
            summary = "매칭별 리뷰 조회",
            description = "특정 매칭에 대한 모든 리뷰를 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<List<ReviewResponse>> getReviewsByMatching(
            @PathVariable Long matchingId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<ReviewResponse> reviews = reviewModel.getReviewsByMatching(matchingId, userPrincipal.getEmail());
        return ApiResponse.success("매칭 리뷰를 성공적으로 조회했습니다.", reviews);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(
            summary = "사용자별 받은 리뷰 조회",
            description = "특정 사용자가 받은 모든 리뷰를 조회합니다."
    )
    public ApiResponse<List<ReviewResponse>> getReviewsByUser(@PathVariable Long userId) {
        
        List<ReviewResponse> reviews = reviewModel.getReviewsByUser(userId);
        return ApiResponse.success("사용자 리뷰를 성공적으로 조회했습니다.", reviews);
    }
    
    @GetMapping("/my")
    @Operation(
            summary = "내가 받은 리뷰 조회",
            description = "현재 사용자가 받은 모든 리뷰를 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<ReviewResponse> reviews = reviewModel.getReviewsByUser(userPrincipal.getId());
        return ApiResponse.success("내 리뷰를 성공적으로 조회했습니다.", reviews);
    }
}
