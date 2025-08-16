package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.model.MatchingModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "매칭", description = "매칭 수락/거절 관련 API")
public class MatchingController {
    
    private final MatchingModel matchingModel;
    
    @PostMapping("/{matchingId}/accept")
    @Operation(
            summary = "매칭 수락",
            description = "매칭을 수락합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Long> acceptMatching(
            @PathVariable Long matchingId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long id = matchingModel.acceptMatching(matchingId, userPrincipal.getEmail());
        return ApiResponse.success("매칭이 수락되었습니다.", id);
    }
    
    @PostMapping("/{matchingId}/reject")
    @Operation(
            summary = "매칭 거절",
            description = "매칭을 거절합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Long> rejectMatching(
            @PathVariable Long matchingId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Long id = matchingModel.rejectMatching(matchingId, userPrincipal.getEmail());
        return ApiResponse.success("매칭이 거절되었습니다.", id);
    }
    
    @GetMapping("/{matchingId}")
    @Operation(
            summary = "매칭 조회",
            description = "매칭 정보를 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Matching> getMatching(
            @PathVariable Long matchingId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        Matching matching = matchingModel.getMatching(matchingId, userPrincipal.getEmail());
        return ApiResponse.success("매칭 정보를 성공적으로 조회했습니다.", matching);
    }
}
