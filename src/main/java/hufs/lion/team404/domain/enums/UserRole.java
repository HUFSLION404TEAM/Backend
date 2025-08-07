package hufs.lion.team404.domain.enums;

public enum UserRole {
	TEMP("미선택"), STUDENT("대학생"), STORE("소상공인"), ADMIN("관리자");
	private final String value;

	UserRole(String value) {
		this.value = value;
	}

	public String getKey() {
		return value;
	}
}
