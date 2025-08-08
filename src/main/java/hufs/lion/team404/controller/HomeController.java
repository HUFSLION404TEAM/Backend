package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "시스템", description = "시스템 상태 확인 API")
public class HomeController {
    
    @GetMapping("/health")
    @Operation(summary = "헬스체크", description = "서버 상태를 확인합니다.")
    public ApiResponse<String> health() {
        return ApiResponse.success("OK", "서버가 정상적으로 작동 중입니다.");
    }
}
