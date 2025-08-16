package hufs.lion.team404.domain.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class ApplicationSaveRequest {
	private String title;
	private Map<String, String> answers;
}
