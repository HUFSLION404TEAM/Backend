package hufs.lion.team404.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.util.Map;

@Getter
public class ApplicationStartRequest {
	@NotNull private Long storeId;
	private String title;
	private Map<String, String> answers;
}

