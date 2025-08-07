package hufs.lion.team404.domain.dto.request;

import java.util.Date;

import lombok.Getter;

@Getter
public class StoreCreateRequestDto {
	private String name;
	private String type;
	private String address;
	private String phone;
	private String businessNumber;
	private String introduction;
}
