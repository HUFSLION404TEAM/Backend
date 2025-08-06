package hufs.lion.team404.oauth;

public enum UserRole {
	STUDENT("대학생"), STORE("소상공인"),  ADMIN("관리자");
	private final String value;

	UserRole(String value) {
		this.value = value;
	}

	public String getKey() {
		return value;
	}
}
