package hufs.lion.team404.model;

import java.util.List;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import hufs.lion.team404.domain.dto.request.StoreUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.StoreReadResponseDto;
import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.domain.dto.response.StoreReadResponseDto;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.service.StoreService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class StoreModel {
	private final StoreService storeService;
	private final UserService userService;

	public void createStore(StoreCreateRequestDto storeCreateRequestDto, Long user_id) {
		User user = userService.findById(user_id).orElseThrow(() -> new NotFoundException("User not found"));

		Store store = Store.builder()
			.storeName(storeCreateRequestDto.getName())
			.address(storeCreateRequestDto.getAddress())
			.contact(storeCreateRequestDto.getPhone())
			.category(storeCreateRequestDto.getType())
			.introduction(storeCreateRequestDto.getIntroduction())
			.businessNumber(storeCreateRequestDto.getBusinessNumber())
			.user(user)
			.build();

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
}