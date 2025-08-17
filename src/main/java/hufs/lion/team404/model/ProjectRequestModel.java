package hufs.lion.team404.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.request.ProjectRequestCreateRequestDto;
import hufs.lion.team404.domain.dto.request.ProjectRequestUpdateRequestDto;
import hufs.lion.team404.domain.entity.ChatRoom;
import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.ProjectRequest;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.exception.StoreNotFoundException;
import hufs.lion.team404.service.ChatRoomService;
import hufs.lion.team404.service.MatchingService;
import hufs.lion.team404.service.ProjectRequestFileService;
import hufs.lion.team404.service.ProjectRequestService;
import hufs.lion.team404.service.StoreService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectRequestModel {
	private final ProjectRequestService projectRequestService;
	private final ProjectRequestFileService projectRequestFileService;
	private final UserService userService;
	private final StoreService storeService;
	private final MatchingService matchingService;
	private final ChatRoomService chatRoomService;

	@Transactional
	public Long createProjectRequest(ProjectRequestCreateRequestDto dto, Long id, List<MultipartFile> files) {
		Store store = storeService.findByUserId(id)
			.orElseThrow(() -> new StoreNotFoundException("Store not found: " + id));

		// 채팅방 정보 가져오기 (이미 존재하는 채팅방)
		ChatRoom chatRoom = chatRoomService.findById(dto.getChatRoomId())
			.orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND, "채팅방을 찾을 수 없습니다."));

		// 채팅방의 학생 정보 가져오기
		Student student = chatRoom.getStudent();

		LocalDate startDate = dto.getStartDate();
		LocalDate endDate = dto.getEndDate();
		if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "마감일은 시작일보다 빠를 수 없습니다.");
		}

		ProjectRequest projectRequest = ProjectRequest.builder()
			.store(store)
			.title(dto.getTitle())
			.projectOverview(dto.getProjectOverview())
			.startDate(startDate)
			.endDate(endDate)
			.estimatedDuration(dto.getEstimatedDuration())
			.detailedTasks(dto.getDetailedTasks())
			.requiredSkills(dto.getRequiredSkills())
			.budget(dto.getBudget())
			.paymentMethod(dto.getPaymentMethod())
			.workLocation(dto.getWorkLocation())
			.workSchedule(dto.getWorkSchedule())
			.preferredMajor(dto.getPreferredMajor())
			.minGrade(dto.getMinGrade())
			.requiredExperience(dto.getRequiredExperience())
			.additionalNotes(dto.getAdditionalNotes())
			.status(ProjectRequest.Status.ACTIVE)
			.build();

		ProjectRequest savedProjectRequest = projectRequestService.save(projectRequest);

		// ProjectRequest 생성 시 Matching도 함께 생성
		Matching matching = new Matching();
		matching.setProjectRequest(savedProjectRequest);
		matching.setMatchedBy(Matching.MatchedBy.STORE_OFFER); // 가게에서 의뢰한 경우
		matching.setStatus(Matching.Status.PENDING);
		matching.setOfferedAt(java.time.LocalDateTime.now());

		matchingService.save(matching);

		// 첨부파일 추가 관련 로직
		projectRequestFileService.update(projectRequest, files);

		return projectRequest.getId();
	}

	// 의뢰서 조회
	@Transactional(readOnly = true)
	public ProjectRequest getProjectRequest(Long id) {
		return projectRequestService.findById(id).orElseThrow(() -> new IllegalArgumentException("의뢰서를 찾을 수 없습니다."));
	}

	// 의뢰서 수정
	@Transactional
	public ProjectRequest update(Long projectRequestId, ProjectRequestUpdateRequestDto dto, Long userId,
		List<MultipartFile> files, boolean clearFiles) {

		ProjectRequest updated = projectRequestService.update(projectRequestId, dto, userId);

		if (clearFiles) {
			projectRequestFileService.deleteAllByProjectRequestId(updated.getId());

			if (files != null && !files.isEmpty()) {
				projectRequestFileService.update(updated, files);
			}
		}

		projectRequestFileService.update(updated, files);
		return updated;
	}

	// 의뢰서 삭제
	public void deleteProjectRequest(Long projectRequestId, Long id) {
		User user = userService.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

		Store store = user.getStore();
		if (store == null) {
			throw new StoreNotFoundException("상점을 찾을 수 없습니다.");
		}

		ProjectRequest projectRequest = projectRequestService.findById(projectRequestId)
			.orElseThrow(() -> new IllegalArgumentException("의뢰서를 찾을 수 없습니다."));

		if (!projectRequest.getStore().getId().equals(store.getId())) {
			throw new IllegalArgumentException("본인의 의뢰서만 삭제할 수 있습니다.");
		}

		projectRequestService.deleteById(projectRequestId);
	}
}
