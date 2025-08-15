package hufs.lion.team404.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreSearchRequestDto {
	private String q;
	private String city;
	private String district;
	private String category;
	private String status;
	private String sort;
	private Integer limit;
}