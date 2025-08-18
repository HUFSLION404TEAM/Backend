package hufs.lion.team404.model;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hufs.lion.team404.domain.dto.request.ApplicationSaveRequestDto;
import hufs.lion.team404.domain.dto.request.ApplicationCreateRequestDto;
import hufs.lion.team404.domain.dto.response.ApplicationResponse;
import hufs.lion.team404.domain.entity.Application;
import hufs.lion.team404.domain.entity.ChatRoom;
import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.ApplicationFileService;
import hufs.lion.team404.service.ApplicationService;
import hufs.lion.team404.service.ChatRoomService;
import hufs.lion.team404.service.MatchingService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationModel {
    private final ApplicationService applicationService;
    private final ApplicationFileService applicationFileService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final MatchingService matchingService;

    @Transactional
    public Long createApplication(ApplicationCreateRequestDto dto, Long userId, List<MultipartFile> files) {
        // 사용자 정보를 먼저 가져온 후, 해당 사용자가 학생인지 확인
        User user = userService.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 사용자의 학생 정보 확인
        Student student = user.getStudent();
        if (student == null) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "학생만 지원서를 작성할 수 있습니다.");
        }

        // 채팅방 정보 가져오기 (이미 존재하는 채팅방)
        ChatRoom chatRoom = chatRoomService.findById(dto.getChatRoomId())
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        // 채팅방의 학생이 현재 사용자의 학생인지 확인
        if (!chatRoom.getStudent().getId().equals(student.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "해당 채팅방의 학생이 아닙니다.");
        }

        // 채팅방의 스토어 정보 가져오기
        Store store = chatRoom.getStore();

        Application application = Application.builder()
            .student(student)
            .title(dto.getTitle())
            .projectName(dto.getProjectName())
            .outline(dto.getOutline())
            .resolveProblem(dto.getResolveProblem())
            .necessity(dto.getNecessity())
            .jobDescription(dto.getJobDescription())
            .result(dto.getResult())
            .requirements(dto.getRequirements())
            .expectedOutcome(dto.getExpectedOutcome())
            .status(Application.Status.ACTIVE)
            .build();

        Application savedApplication = applicationService.save(application);

        // Application 생성 시 Matching도 함께 생성 (채팅방 연결)
        Matching matching = new Matching();
        matching.setApplication(savedApplication);
        matching.setChatRoom(chatRoom); // 채팅방 연결
        matching.setMatchedBy(Matching.MatchedBy.STUDENT_APPLY); // 학생이 지원한 경우
        matching.setStatus(Matching.Status.PENDING);
        matching.setOfferedAt(java.time.LocalDateTime.now());

        matchingService.save(matching);

        // 첨부파일 추가 관련 로직
        if (files != null && !files.isEmpty()) {
            applicationFileService.update(savedApplication, files);
        }

        return savedApplication.getId();
    }

    // 지원서 조회
    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(Long id) {
        Application application = applicationService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없습니다."));
        return ApplicationResponse.from(application);
    }
}
