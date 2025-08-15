package hufs.lion.team404.domain.dto.request;

import java.util.Date;

import lombok.Getter;

@Getter
public class StudentCreateRequestDto {
	private String name;
	private String birth;
	private String phone;
	private String email;
	private String university;
	private String introduction;
	private String career;
	private Boolean isPublic;
	private Boolean isEmployment;
	private String region;
}
