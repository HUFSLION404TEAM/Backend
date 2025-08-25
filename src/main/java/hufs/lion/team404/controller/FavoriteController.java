package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.FavoriteToggleRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.FavoriteResponse;
import hufs.lion.team404.domain.entity.Favorite;
import hufs.lion.team404.domain.entity.Recruiting;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import hufs.lion.team404.repository.UserRepository;
import hufs.lion.team404.service.FavoriteService;
import hufs.lion.team404.service.RecruitingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "찜하기", description = "찜하기 관련 API")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;
    private final RecruitingService recruitingService;

    @PostMapping("/students/{studentUserId}")
    @Operation(
        summary = "학생 찜하기/취소",
        description = "학생을 찜하거나 찜을 취소합니다. 이미 찜한 학생이면 찜 취소, 찜하지 않은 학생이면 찜하기가 됩니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<String> toggleStudentFavorite(
            @PathVariable Long studentUserId,
            @Valid @RequestBody FavoriteToggleRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 대상 학생 유저 정보 조회
        User targetStudentUser = userRepository.findById(studentUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "대상 학생을 찾을 수 없습니다."));
        
        // 학생인지 확인
        if (targetStudentUser.getStudent() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "대상 사용자는 학생이 아닙니다.");
        }
        
        // 자기 자신을 찜할 수 없음
        if (userId.equals(studentUserId)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "자기 자신을 찜할 수 없습니다.");
        }
        
        // 이미 찜했는지 확인
        Optional<Favorite> existingFavorite = favoriteService.findByUserIdAndTargetStudentUserId(userId, studentUserId);
        
        if (existingFavorite.isPresent()) {
            // 찜 취소
            favoriteService.deleteById(existingFavorite.get().getId());
            return ApiResponse.success("학생 찜을 취소했습니다.", "REMOVED");
        } else {
            // 찜하기
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setTargetStudentUser(targetStudentUser);
            favorite.setFavoriteType(Favorite.FavoriteType.STUDENT_USER);
            favorite.setMemo(request.getMemo());
            favorite.setIsNotificationEnabled(false);
            
            favoriteService.save(favorite);
            return ApiResponse.success("학생을 찜했습니다.", "ADDED");
        }
    }

    @PostMapping("/recruitings/{recruitingId}")
    @Operation(
        summary = "구인글 찜하기/취소",
        description = "구인글을 찜하거나 찜을 취소합니다. 이미 찜한 구인글이면 찜 취소, 찜하지 않은 구인글이면 찜하기가 됩니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<String> toggleRecruitingFavorite(
            @PathVariable Long recruitingId,
            @Valid @RequestBody FavoriteToggleRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 대상 구인글 정보 조회
        Recruiting targetRecruiting = recruitingService.findById(recruitingId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND, "구인글을 찾을 수 없습니다."));
        
        // 자기 자신의 구인글을 찜할 수 없음
        if (targetRecruiting.getStore().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "자신의 구인글을 찜할 수 없습니다.");
        }
        
        // 이미 찜했는지 확인
        Optional<Favorite> existingFavorite = favoriteService.findByUserIdAndTargetRecruitingId(userId, recruitingId);
        
        if (existingFavorite.isPresent()) {
            // 찜 취소
            favoriteService.deleteById(existingFavorite.get().getId());
            return ApiResponse.success("구인글 찜을 취소했습니다.", "REMOVED");
        } else {
            // 찜하기
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setTargetRecruiting(targetRecruiting);
            favorite.setFavoriteType(Favorite.FavoriteType.RECRUITING);
            favorite.setMemo(request.getMemo());
            favorite.setIsNotificationEnabled(false);
            
            favoriteService.save(favorite);
            return ApiResponse.success("구인글을 찜했습니다.", "ADDED");
        }
    }

    @GetMapping("/students")
    @Operation(
        summary = "찜한 학생 목록 조회",
        description = "현재 사용자가 찜한 학생 목록을 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<List<FavoriteResponse>> getFavoriteStudents(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        List<Favorite> favorites = favoriteService.findByUserIdAndFavoriteType(userId, Favorite.FavoriteType.STUDENT_USER);
        
        List<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(FavoriteResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ApiResponse.success("찜한 학생 목록을 조회했습니다.", favoriteResponses);
    }

    @GetMapping("/recruitings")
    @Operation(
        summary = "찜한 구인글 목록 조회",
        description = "현재 사용자가 찜한 구인글 목록을 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<List<FavoriteResponse>> getFavoriteRecruitings(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        List<Favorite> favorites = favoriteService.findByUserIdAndFavoriteType(userId, Favorite.FavoriteType.RECRUITING);
        
        List<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(FavoriteResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ApiResponse.success("찜한 구인글 목록을 조회했습니다.", favoriteResponses);
    }

    @GetMapping("/check/recruiting/{recruitingId}")
    @Operation(
        summary = "구인글 찜 여부 확인",
        description = "현재 사용자가 특정 구인글을 찜했는지 확인합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Boolean> checkRecruitingFavorite(
            @PathVariable Long recruitingId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        boolean isFavorite = favoriteService.existsByUserIdAndTargetRecruitingId(userId, recruitingId);
        
        return ApiResponse.success("구인글 찜 여부를 확인했습니다.", isFavorite);
    }

    @GetMapping("/check/student/{studentUserId}")
    @Operation(
        summary = "학생 찜 여부 확인",
        description = "현재 사용자가 특정 학생을 찜했는지 확인합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Boolean> checkStudentFavorite(
            @PathVariable Long studentUserId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        boolean isFavorite = favoriteService.existsByUserIdAndTargetStudentUserId(userId, studentUserId);
        
        return ApiResponse.success("학생 찜 여부를 확인했습니다.", isFavorite);
    }
}
