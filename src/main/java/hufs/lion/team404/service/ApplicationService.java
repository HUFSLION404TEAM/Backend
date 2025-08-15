package hufs.lion.team404.service;

import hufs.lion.team404.domain.dto.request.ApplicationSaveRequest;
import hufs.lion.team404.domain.dto.request.ApplicationStartRequest;
import hufs.lion.team404.domain.dto.response.ApplicationResponse;
import hufs.lion.team404.domain.entity.Application;
import hufs.lion.team404.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

	private final ApplicationRepository repo;
	@Transactional
	public ApplicationResponse start(Long studentId, ApplicationStartRequest req) {
		var existingDraft = repo.findTopByStudentIdAndStoreIdAndStatusOrderByIdDesc(
			studentId, req.getStoreId(), Application.Status.DRAFT
		);
		if (existingDraft.isPresent()) {
			return ApplicationResponse.from(existingDraft.get());
		}
		Application created = Application.builder()
			.studentId(studentId)
			.storeId(req.getStoreId())
			.title(req.getTitle())
			.status(Application.Status.DRAFT)
			.build();

		Map<String, String> a = req.getAnswers();
		if (a != null && !a.isEmpty()) {
			if (created.getAnswers() == null) {
				created.setAnswers(new HashMap<>());
			}
			created.getAnswers().putAll(a);
		}

		Application saved = repo.save(created);
		return ApplicationResponse.from(saved);
	}
	@Transactional
	public ApplicationResponse saveDraft(Long studentId, Long applicationId, ApplicationSaveRequest req) {
		Application a = repo.findByIdAndStudentId(applicationId, studentId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "지원서를 찾을 수 없습니다."));

		if (a.getStatus() != Application.Status.DRAFT) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "제출되었거나 삭제된 지원서는 수정할 수 없습니다.");
		}

		if (req.getTitle() != null) a.setTitle(req.getTitle());

		if (req.getAnswers() != null && !req.getAnswers().isEmpty()) {
			if (a.getAnswers() == null) a.setAnswers(new HashMap<>());
			a.getAnswers().putAll(req.getAnswers());
		}

		return ApplicationResponse.from(a);
	}
	@Transactional
	public ApplicationResponse submit(Long studentId, Long applicationId) {
		Application a = repo.findByIdAndStudentId(applicationId, studentId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "지원서를 찾을 수 없습니다."));

		if (a.getStatus() != Application.Status.DRAFT) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 제출되었거나 삭제된 지원서입니다.");
		}

		Map<String, String> m = a.getAnswers();
		require(m, "personal.name");
		require(m, "personal.university");
		require(m, "personal.major");
		require(m, "personal.phone");
		require(m, "personal.email");

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

	private static void require(Map<String, String> m, String key) {
		if (m == null || !m.containsKey(key) || m.get(key) == null || m.get(key).isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 항목 누락: " + key);
		}
	}
}


