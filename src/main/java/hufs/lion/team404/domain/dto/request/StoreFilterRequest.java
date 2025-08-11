package hufs.lion.team404.domain.dto.request;

/**
 * 소상공인 목록/필터 조회용 요청 DTO
 * - keyword: storeName / introduction LIKE 검색
 * - category: 정확히 일치
 * - address: 부분 일치
 */
public record StoreFilterRequest(
	String keyword,
	String category,
	String address
) {

	public StoreFilterRequest {
		keyword  = normalize(keyword);
		category = normalize(category);
		address  = normalize(address);
	}

	private static String normalize(String s) {
		if (s == null) return null;
		s = s.trim();
		return s.isEmpty() ? null : s;
	}
}