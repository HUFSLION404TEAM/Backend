package hufs.lion.team404.model;

import java.util.List;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.domain.dto.request.StoreUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.StoreReadResponseDto;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.exception.CustomException;
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

		// 이미 같은 사업자번호로 등록된 업체가 있는지 확인
		if (storeService.existsByBusinessNumber(storeCreateRequestDto.getBusinessNumber())) {
			throw new CustomException(ErrorCode.STORE_USER_ALREADY_HAVE, "이미 등록된 사업자번호입니다.");
		}

		Store store = Store.builder()
			.businessNumber(storeCreateRequestDto.getBusinessNumber())
			.user(user)
			.storeName(storeCreateRequestDto.getName())
			.address(storeCreateRequestDto.getAddress())
			.contact(storeCreateRequestDto.getPhone())
			.category(storeCreateRequestDto.getType())
			.introduction(storeCreateRequestDto.getIntroduction())
			.build();

		user.setUserRole(UserRole.STORE);

		storeService.save(store);
	}

	public StoreReadResponseDto getStoreByBusinessNumber(String businessNumber) {
		Store store = storeService.findById(businessNumber)
			.orElseThrow(() -> new NotFoundException("Store not found: businessNumber=" + businessNumber));

		return StoreReadResponseDto.fromEntity(store);
	}

	public List<StoreReadResponseDto> getAllStores() {
		return storeService.findAll().stream()
			.map(StoreReadResponseDto::fromEntity)
			.toList();
	}

	public StoreReadResponseDto updateStore(String businessNumber, StoreUpdateRequestDto dto, Long userId) {
		Store updated = storeService.updateStore(businessNumber, dto, userId);
		return StoreReadResponseDto.fromEntity(updated);
	}

	public void deleteStore(String businessNumber, Long userId) {
		storeService.deleteStore(businessNumber, userId);
	}
}