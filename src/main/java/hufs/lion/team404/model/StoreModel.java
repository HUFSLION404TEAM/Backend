package hufs.lion.team404.model;

import java.util.List;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.CONFLICT;
import org.springframework.transaction.annotation.Transactional;
import hufs.lion.team404.domain.dto.request.StoreUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.StoreReadResponseDto;
import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.domain.dto.response.StoreReadResponseDto;
import hufs.lion.team404.domain.dto.request.StoreFilterRequest;
import hufs.lion.team404.domain.dto.response.StoreSummaryResponse;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.service.StoreService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class StoreModel {
	private final StoreService storeService;
	private final UserService userService;

	@Transactional
	public void createStore(StoreCreateRequestDto dto, Long userId) {
		User user = userService.findById(userId)
			.orElseThrow(() -> new NotFoundException("User not found"));
		if (storeService.hasStoreForUser(userId)) {
			throw new ResponseStatusException(CONFLICT, "이미 해당 사용자에 등록된 업체가 있습니다.");
		}
		if (storeService.existsByBusinessNumber(dto.getBusinessNumber())) {
			throw new ResponseStatusException(CONFLICT, "이미 등록된 사업자번호입니다.");
		}
		Store store = Store.builder()
			.storeName(dto.getName())
			.address(dto.getAddress())
			.contact(dto.getPhone())
			.category(dto.getType())
			.introduction(dto.getIntroduction())
			.businessNumber(dto.getBusinessNumber())
			.user(user)
			.build();
		user.setUserRole(UserRole.STORE);
		storeService.save(store);
	}
	public StoreReadResponseDto getStoreById(Long storeId) {
		Store store = storeService.findById(storeId)
			.orElseThrow(() -> new NotFoundException("Store not found: id=" + storeId));

		return StoreReadResponseDto.fromEntity(store);
	}
	public List<StoreReadResponseDto> getAllStores() {
		return storeService.findAll().stream()
			.map(StoreReadResponseDto::fromEntity)
			.toList();
	}
	public StoreReadResponseDto updateStore(Long storeId, StoreUpdateRequestDto dto, Long userId) {
		Store updated = storeService.updateStore(storeId, dto, userId);
		return StoreReadResponseDto.fromEntity(updated);
	}
	public void deleteStore(Long storeId, Long userId) {
		storeService.deleteStore(storeId, userId);
	}
	public Page<StoreSummaryResponse> getStoresPage(Pageable pageable) {
		return storeService.listAll(pageable);
	}
	public Page<StoreSummaryResponse> searchStores(StoreFilterRequest filter, Pageable pageable) {
		return storeService.listWithFilter(filter, pageable);
	}


}