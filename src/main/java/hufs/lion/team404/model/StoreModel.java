package hufs.lion.team404.model;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
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
}
