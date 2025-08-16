package hufs.lion.team404.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApplicationStartRequestDto {
	@NotNull private Long storeId;
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
