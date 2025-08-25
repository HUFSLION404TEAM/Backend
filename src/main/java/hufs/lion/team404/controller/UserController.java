package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.UserResponse;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "사용자", description = "사용자 관련 API")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @Operation(
        summary = "모든 사용자 조회",
        description = "시스템에 등록된 모든 사용자 목록을 조회합니다. 토큰이 필요하지 않습니다."
    )
    public ApiResponse<List<UserResponse>> getAllUsers() {
        log.info("모든 사용자 조회 요청");
        
        List<User> users = userService.findAll();
        List<UserResponse> userResponses = users.stream()
            .map(this::convertToUserResponse)
            .collect(Collectors.toList());
        
        log.info("총 {} 명의 사용자를 조회했습니다.", userResponses.size());
        
        return ApiResponse.success("모든 사용자 조회가 완료되었습니다.", userResponses);
    }
    
    @GetMapping("/{userId}")
    @Operation(
        summary = "특정 사용자 조회",
        description = "사용자 ID로 특정 사용자 정보를 조회합니다. 토큰이 필요하지 않습니다."
    )
    public ApiResponse<UserResponse> getUserById(@PathVariable Long userId) {
        log.info("사용자 ID {} 조회 요청", userId);
        
        User user = userService.findById(userId)
            .orElseThrow(() -> {
                log.error("사용자 ID {}를 찾을 수 없습니다.", userId);
                return new RuntimeException("사용자를 찾을 수 없습니다.");
            });
        
        UserResponse userResponse = convertToUserResponse(user);
        
        log.info("사용자 ID {} 조회 완료: {}", userId, user.getName());
        
        return ApiResponse.success("사용자 조회가 완료되었습니다.", userResponse);
    }
    
    @GetMapping("/email/{email}")
    @Operation(
        summary = "이메일로 사용자 조회",
        description = "이메일 주소로 사용자 정보를 조회합니다. 토큰이 필요하지 않습니다."
    )
    public ApiResponse<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("이메일 {} 로 사용자 조회 요청", email);
        
        User user = userService.findByEmail(email)
            .orElseThrow(() -> {
                log.error("이메일 {}에 해당하는 사용자를 찾을 수 없습니다.", email);
                return new RuntimeException("사용자를 찾을 수 없습니다.");
            });
        
        UserResponse userResponse = convertToUserResponse(user);
        
        log.info("이메일 {} 로 사용자 조회 완료: {}", email, user.getName());
        
        return ApiResponse.success("사용자 조회가 완료되었습니다.", userResponse);
    }
    
    @GetMapping("/count")
    @Operation(
        summary = "사용자 총 개수 조회",
        description = "시스템에 등록된 전체 사용자 수를 조회합니다. 토큰이 필요하지 않습니다."
    )
    public ApiResponse<Long> getUserCount() {
        log.info("사용자 총 개수 조회 요청");
        
        List<User> users = userService.findAll();
        long count = users.size();
        
        log.info("전체 사용자 수: {} 명", count);
        
        return ApiResponse.success("사용자 총 개수 조회가 완료되었습니다.", count);
    }
    
    // User 엔티티를 UserResponse로 변환하는 헬퍼 메서드
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .profileImage(user.getProfileImage())
            .socialProvider(user.getSocialProvider())
            .userRole(user.getUserRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .hasStudent(user.getStudent() != null)
            .hasStores(user.getStores() != null && !user.getStores().isEmpty())
            .build();
    }
}
