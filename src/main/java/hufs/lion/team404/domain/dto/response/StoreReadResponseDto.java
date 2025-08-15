package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreReadResponseDto {
	private Long id;
	private String storeName;
	private String businessNumber;
	private String address;
	private String contact;
	private String category;
	private String introduction;

	public static StoreReadResponseDto fromEntity(Store store) {
		return StoreReadResponseDto.builder()
			.id(store.getId())
			.storeName(store.getStoreName())
			.businessNumber(store.getBusinessNumber())
			.address(store.getAddress())
			.contact(store.getContact())
			.category(store.getCategory())
			.introduction(store.getIntroduction())
			.build();
	}
}