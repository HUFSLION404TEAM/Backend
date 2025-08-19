package hufs.lion.team404.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationSaveRequestDto {
	private Long chatRoomId;  // API 입력용 (수정시는 필수 아님)
	private String storeBusinessNumber; // 내부 처리용
	private String title;
	private String projectName;
	private String outline;
	private String resolveProblem;
	private String necessity;
	private String jobDescription;
	private String result;
	private String requirements;
	private String expectedOutcome;
}