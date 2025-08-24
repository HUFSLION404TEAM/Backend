package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.FavoriteToggleRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.FavoriteResponse;
import hufs.lion.team404.domain.entity.Favorite;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import hufs.lion.team404.repository.StoreRepository;
import hufs.lion.team404.repository.StudentRepository;
import hufs.lion.team404.repository.UserRepository;
import hufs.lion.team404.service.FavoriteService;
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
@CrossOrigin(origins = "*")
@Tag(name = "찜하기", description = "찜하기 관련 API")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StoreRepository storeRepository;

    @PostMapping("/students/{studentId}")
    @Operation(
        summary = "학생 찜하기/취소",
        description = "학생을 찜하거나 찜을 취소합니다. 이미 찜한 학생이면 찜 취소, 찜하지 않은 학생이면 찜하기가 됩니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<String> toggleStudentFavorite(
            @PathVariable Long studentId,
            @Valid @RequestBody FavoriteToggleRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 대상 학생 정보 조회
        Student targetStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));
        
        // 자기 자신을 찜할 수 없음
        if (user.getStudent() != null && user.getStudent().getId().equals(studentId)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        
        // 이미 찜했는지 확인
        Optional<Favorite> existingFavorite = favoriteService.findByUserIdAndTargetStudentId(userId, studentId);
        
        if (existingFavorite.isPresent()) {
            // 찜 취소
            favoriteService.deleteById(existingFavorite.get().getId());
            return ApiResponse.success("학생 찜을 취소했습니다.", "REMOVED");
        } else {
            // 찜하기
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setTargetStudent(targetStudent);
            favorite.setFavoriteType(Favorite.FavoriteType.STUDENT);
            favorite.setMemo(request.getMemo());
            favorite.setIsNotificationEnabled(false);
            
            favoriteService.save(favorite);
            return ApiResponse.success("학생을 찜했습니다.", "ADDED");
        }
    }

    @PostMapping("/stores/{storeId}")
    @Operation(
        summary = "가게 찜하기/취소",
        description = "가게를 찜하거나 찜을 취소합니다. 이미 찜한 가게면 찜 취소, 찜하지 않은 가게면 찜하기가 됩니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<String> toggleStoreFavorite(
            @PathVariable Long storeId,
            @Valid @RequestBody FavoriteToggleRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 대상 가게 정보 조회
        Store targetStore = storeRepository.findById(storeId.toString())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        
        // 자기 자신의 가게를 찜할 수 없음
        if (user.getStores().stream().anyMatch(store -> store.getBusinessNumber().equals(storeId.toString()))) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        
        // 이미 찜했는지 확인
        Optional<Favorite> existingFavorite = favoriteService.findByUserIdAndTargetStoreBusinessNumber(userId, storeId.toString());
        
        if (existingFavorite.isPresent()) {
            // 찜 취소
            favoriteService.deleteById(existingFavorite.get().getId());
            return ApiResponse.success("가게 찜을 취소했습니다.", "REMOVED");
        } else {
            // 찜하기
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setTargetStore(targetStore);
            favorite.setFavoriteType(Favorite.FavoriteType.STORE);
            favorite.setMemo(request.getMemo());
            favorite.setIsNotificationEnabled(false);
            
            favoriteService.save(favorite);
            return ApiResponse.success("가게를 찜했습니다.", "ADDED");
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
        
        List<Favorite> favorites = favoriteService.findByUserIdAndFavoriteType(userId, Favorite.FavoriteType.STUDENT);
        
        List<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(FavoriteResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ApiResponse.success("찜한 학생 목록을 조회했습니다.", favoriteResponses);
    }

    @GetMapping("/stores")
    @Operation(
        summary = "찜한 가게 목록 조회",
        description = "현재 사용자가 찜한 가게 목록을 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<List<FavoriteResponse>> getFavoriteStores(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long userId = userPrincipal.getId();
        
        List<Favorite> favorites = favoriteService.findByUserIdAndFavoriteType(userId, Favorite.FavoriteType.STORE);
        
        List<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(FavoriteResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ApiResponse.success("찜한 가게 목록을 조회했습니다.", favoriteResponses);
    }
}
