package hufs.lion.team404.controller;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.server.ResponseStatusException;

import hufs.lion.team404.domain.dto.request.StoreFilterRequest;
import hufs.lion.team404.domain.dto.request.StoreUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.StoreReadResponseDto;
import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.StoreSummaryResponse;
import hufs.lion.team404.model.StoreModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "업체", description = "업체 관련 API")
public class StoreController {

	private final StoreModel storeModel;

	private static final Set<String> ALLOWED_SORT_KEYS = Set.of("id", "storeName", "createdAt");

	private Pageable sanitize(Pageable p) {
		var safeOrders = p.getSort().stream()
			.map(o -> ALLOWED_SORT_KEYS.contains(o.getProperty())
				? o
				: new Sort.Order(o.getDirection(), "createdAt"))
			.collect(Collectors.toList());

		if (safeOrders.isEmpty()) {
			safeOrders = List.of(new Sort.Order(Sort.Direction.DESC, "createdAt"));
		}
		return PageRequest.of(p.getPageNumber(), p.getPageSize(), Sort.by(safeOrders));
	}

	@PostMapping("/create")
	@Operation(
		summary = "업체 정보 생성",
		description = "새로운 업체 정보를 생성합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<Void> createStore(
		@AuthenticationPrincipal UserPrincipal authentication,
		@Valid @RequestBody StoreCreateRequestDto dto
	) {
		if (authentication == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
		}
		storeModel.createStore(dto, authentication.getId());
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
	@Operation(
		summary = "업체 정보 수정",
		description = "업체의 일부 정보를 수정합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
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

	@GetMapping("/page")
	@Operation(summary = "업체 목록 조회(페이지)", description = "페이지네이션으로 업체 목록을 조회합니다.")
	public ApiResponse<Page<StoreSummaryResponse>> getStoresPage(
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		pageable = sanitize(pageable);
		return ApiResponse.success(storeModel.getStoresPage(pageable));
	}

	// 필터 조회 (keyword/category/address + 페이지네이션)
	@GetMapping("/filter")
	@Operation(summary = "업체 필터 조회", description = "키워드, 카테고리, 주소로 필터링하여 조회합니다.")
	public ApiResponse<Page<StoreSummaryResponse>> searchStores(
		@ModelAttribute StoreFilterRequest filter,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		pageable = sanitize(pageable) ;
		return ApiResponse.success(storeModel.searchStores(filter, pageable));
	}


}
