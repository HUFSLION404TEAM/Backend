package hufs.lion.team404.domain.dto.request;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
@Getter @Setter @NoArgsConstructor
public class ApplicationSaveRequestDto {
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

