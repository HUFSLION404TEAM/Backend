package hufs.lion.team404.service;
import hufs.lion.team404.domain.dto.request.ApplicationSaveRequestDto;
import hufs.lion.team404.domain.dto.request.ApplicationStartRequestDto;
import hufs.lion.team404.domain.dto.response.ApplicationResponse;
import hufs.lion.team404.domain.entity.Application;
import hufs.lion.team404.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

	private final ApplicationRepository repo;

	@Transactional
	public ApplicationResponse start(Long studentId, ApplicationStartRequestDto req) {
		var existingDraft = repo.findTopByStudentIdAndStoreIdAndStatusOrderByIdDesc(
			studentId, req.getStoreId(), Application.Status.DRAFT
		);
		if (existingDraft.isPresent()) {
			return ApplicationResponse.from(existingDraft.get());
		}

		Application a = Application.builder()
			.studentId(studentId)
			.storeId(req.getStoreId())
			.status(Application.Status.DRAFT)
			.title(req.getTitle())
			.build();

		copyFromStartDto(a, req);

		return ApplicationResponse.from(repo.save(a));
	}


	@Transactional
	public ApplicationResponse saveDraft(Long studentId, Long applicationId, ApplicationSaveRequestDto req) {
		Application a = repo.findByIdAndStudentId(applicationId, studentId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "지원서를 찾을 수 없습니다."));

		if (a.getStatus() != Application.Status.DRAFT) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "제출되었거나 삭제된 지원서는 수정할 수 없습니다.");
		}

		applyNonNull(a::setTitle,           req.getTitle());
		applyNonNull(a::setProjectName,     req.getProjectName());
		applyNonNull(a::setOutline,         req.getOutline());
		applyNonNull(a::setResolveProblem,  req.getResolveProblem());
		applyNonNull(a::setNecessity,       req.getNecessity());
		applyNonNull(a::setJobDescription,  req.getJobDescription());
		applyNonNull(a::setResult,          req.getResult());
		applyNonNull(a::setRequirements,    req.getRequirements());
		applyNonNull(a::setExpectedOutcome, req.getExpectedOutcome());

		return ApplicationResponse.from(a);
	}

	@Transactional
	public ApplicationResponse submit(Long studentId, Long applicationId) {
		Application a = repo.findByIdAndStudentId(applicationId, studentId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "지원서를 찾을 수 없습니다."));

		if (a.getStatus() != Application.Status.DRAFT) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 제출되었거나 삭제된 지원서입니다.");
		}

		require(a.getTitle(),           "title");
		require(a.getProjectName(),     "projectName");
		require(a.getOutline(),         "outline");
		require(a.getResolveProblem(),  "resolveProblem");
		require(a.getNecessity(),       "necessity");
		require(a.getJobDescription(),  "jobDescription");
		require(a.getResult(),          "result");
		require(a.getRequirements(),    "requirements");
		require(a.getExpectedOutcome(), "expectedOutcome");

		a.setStatus(Application.Status.SUBMITTED);
		a.setSubmittedAt(LocalDateTime.now());
		return ApplicationResponse.from(a);
	}

	@Transactional
	public void deleteDraft(Long studentId, Long applicationId) {
		Application a = repo.findByIdAndStudentId(applicationId, studentId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "지원서를 찾을 수 없습니다."));

		if (a.getStatus() != Application.Status.DRAFT) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "제출된 지원서는 삭제할 수 없습니다.");
		}

		a.setStatus(Application.Status.DELETED);
		a.setDeletedAt(LocalDateTime.now());
	}


	private static void require(String v, String field) {
		if (v == null || v.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 항목 누락: " + field);
		}
	}
	private static <T> void applyNonNull(Consumer<T> setter, T value) {
		if (value != null) setter.accept(value);
	}
	private static void copyFromStartDto(Application a, ApplicationStartRequestDto req) {
		applyNonNull(a::setProjectName,     req.getProjectName());
		applyNonNull(a::setOutline,         req.getOutline());
		applyNonNull(a::setResolveProblem,  req.getResolveProblem());
		applyNonNull(a::setNecessity,       req.getNecessity());
		applyNonNull(a::setJobDescription,  req.getJobDescription());
		applyNonNull(a::setResult,          req.getResult());
		applyNonNull(a::setRequirements,    req.getRequirements());
		applyNonNull(a::setExpectedOutcome, req.getExpectedOutcome());
	}
}


