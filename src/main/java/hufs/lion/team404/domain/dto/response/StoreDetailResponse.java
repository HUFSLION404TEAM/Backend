package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Store;
import java.time.LocalDateTime;

public record StoreDetailResponse(
	Long id,
	String storeName,
	String category,
	String address,
	String contact,
	String introduction,
	String businessNumber,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static StoreDetailResponse from(Store s) {
		return new StoreDetailResponse(
			s.getId(),
			s.getStoreName(),
			s.getCategory(),
			s.getAddress(),
			s.getContact(),
			s.getIntroduction(),
			s.getBusinessNumber(),
			s.getCreatedAt(),
			s.getUpdatedAt()
		);
	}
}