package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreReadResponseDto {
	private String businessNumber;
	private String storeName;
	private String address;
	private String contact;
	private String category;
	private String introduction;
	private Double temperature;

	public static StoreReadResponseDto fromEntity(Store store) {
		return StoreReadResponseDto.builder()
			.businessNumber(store.getBusinessNumber())
			.storeName(store.getStoreName())
			.address(store.getAddress())
			.contact(store.getContact())
			.category(store.getCategory())
			.introduction(store.getIntroduction())
			.temperature(store.getTemperature())
			.build();
	}
}
