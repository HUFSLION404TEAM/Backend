package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.model.StoreModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "업체", description = "업체 관련 API")
public class StoreController {
	private final StoreModel storeModel;

	@PostMapping("/create")
	@Operation(
		summary = "업체 정보 생성", 
		description = "새로운 업체 정보를 생성합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<Void> createStore(
			@AuthenticationPrincipal UserPrincipal authentication,
			@Valid @RequestBody StoreCreateRequestDto storeCreateRequestDto) {
		
		Long userId = authentication.getId();
		storeModel.createStore(storeCreateRequestDto, userId);
		
		return ApiResponse.success("업체 정보가 성공적으로 생성되었습니다.");
	}
}
