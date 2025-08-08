package hufs.lion.team404.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import hufs.lion.team404.domain.dto.request.StoreUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.StoreReadResponseDto;
import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.model.StoreModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "업체", description = "업체 관련 API")

public class StoreController {
	private final StoreModel storeModel;

	@PostMapping("/")
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
	@GetMapping
	@Operation(summary = "업체 목록 조회", description = "모든 업체 정보를 조회합니다.")
	public ApiResponse<List<StoreReadResponseDto>> getAllStores() {
		return ApiResponse.success(storeModel.getAllStores());
	}
	@GetMapping("/{storeId}")
	@Operation(summary = "업체 단건 조회", description = "업체 ID로 업체 정보를 조회합니다.")
	public ApiResponse<StoreReadResponseDto> getStore(@PathVariable Long storeId) {
		return ApiResponse.success(storeModel.getStoreById(storeId));
	}
	@PutMapping("/{storeId}")
	@Operation(summary = "업체 정보 수정", description = "업체의 일부 정보를 수정합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication"))
	public ApiResponse<StoreReadResponseDto> updateStore(
		@PathVariable Long storeId,
		@AuthenticationPrincipal UserPrincipal authentication,
		@RequestBody @Valid StoreUpdateRequestDto dto) {

		Long userId = authentication.getId();
		return ApiResponse.success(storeModel.updateStore(storeId, dto, userId));
	}
	@DeleteMapping("/{storeId}")
	@Operation(
		summary = "업체 삭제",
		description = "업체를 삭제합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<Void> deleteStore(
		@PathVariable Long storeId,
		@AuthenticationPrincipal UserPrincipal authentication) {

		Long userId = authentication.getId();
		storeModel.deleteStore(storeId, userId);
		return ApiResponse.success("삭제되었습니다.");
	}

}