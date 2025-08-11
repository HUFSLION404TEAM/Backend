package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Store;
import java.time.LocalDateTime;

public record StoreSummaryResponse(
	Long id,
	String storeName,
	String category,
	String address,
	String contact,
	LocalDateTime createdAt
) {
	public static StoreSummaryResponse from(Store s) {
		return new StoreSummaryResponse(
			s.getId(),
			s.getStoreName(),
			s.getCategory(),
			s.getAddress(),
			s.getContact(),
			s.getCreatedAt()
		);
	}
}

