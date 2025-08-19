package hufs.lion.team404.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.request.StudentCreateRequestDto;
import hufs.lion.team404.domain.dto.request.StudentSearchRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.PageResponse;
import hufs.lion.team404.domain.dto.response.StudentMyPageResponse;
import hufs.lion.team404.domain.dto.response.StudentResponse;
import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.MatchingService;
import hufs.lion.team404.service.StudentService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentModel {
	private final StudentService studentService;
	private final UserService userService;
	private final MatchingService matchingService;

	public void createStudent(StudentCreateRequestDto studentCreateRequestDto, Long user_id) {
		User user = userService.findById(user_id).orElseThrow(() -> new NotFoundException("User not found"));

		if (user.getStudent() != null) {
			throw new CustomException(ErrorCode.STUDENT_USER_ALREADY_HAVE);
		}

		Student student = Student.builder()
			.birth(studentCreateRequestDto.getBirth())
			.career(studentCreateRequestDto.getCareer())
			.introduction(studentCreateRequestDto.getIntroduction())
			.isAuthenticated(false)
			.phoneCall(studentCreateRequestDto.getPhone())
			.school(studentCreateRequestDto.getUniversity())
			.user(user)
			.region(studentCreateRequestDto.getRegion())
			.isEmployment(Boolean.TRUE)
			.isPublic(Boolean.TRUE)
			.build();

		user.setUserRole(UserRole.STUDENT);
		studentService.save(student);
	}

	// 학생 정보 검색
	@Transactional(readOnly = true)
	public ApiResponse<PageResponse<StudentResponse>> search(StudentSearchRequestDto request, int page, int size) {

		Page<Student> resultPage = studentService.search(request, page, size);

		Page<StudentResponse> dtoPage = resultPage.map(this::Dto);

		return PageResponse.success("학생 목록을 성공적으로 조회했습니다.", dtoPage);
	}

	private StudentResponse Dto(Student student) {
		User user = student.getUser();
		return StudentResponse.builder()
			.id(student.getId())
			.isAuthenticated(student.getIsAuthenticated())
			.name(user != null ? user.getName() : null)
			.birth(student.getBirth())
			.phoneCall(student.getPhoneCall())
			.email(user != null ? user.getEmail() : null)
			.school(student.getSchool())
			.introduction(student.getIntroduction())
			.career(student.getCareer())
			.isPublic(student.getIsPublic())
			.isEmployment(student.getIsEmployment())
			.region(student.getRegion())
			.build();
	}

	@Transactional(readOnly = true)
	public StudentMyPageResponse getMyPage(Long userId) {
		// 사용자 정보 조회
		User user = userService.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		// 사용자의 모든 업체 조회
		Student student = studentService.findByUserId(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

		// 매칭 이력 조회 (채팅방 기반)
		List<Matching> matchings = matchingService.findByChatRoomStudentUserIdOrderByCreatedAtDesc(userId);

		// 사용자 정보 변환
		StudentMyPageResponse.UserInfo userInfo = StudentMyPageResponse.UserInfo.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.profileImageUrl(user.getProfileImage())
			.joinedAt(user.getCreatedAt())
			.build();

		// 매칭 이력 변환
		List<StudentMyPageResponse.MatchingHistoryInfo> matchingHistory = matchings.stream()
			.map(matching -> {
				String status = getMatchingStatusInKorean(matching.getStatus());
				String projectTitle = getProjectTitle(matching);
				String period = getProjectPeriod(matching);

				return StudentMyPageResponse.MatchingHistoryInfo.builder()
					.matchingId(matching.getId())
					.studentName(matching.getChatRoom().getStudent().getUser().getName())
					.projectTitle(projectTitle)
					.status(status)
					.period(period)
					.matchedAt(matching.getOfferedAt())
					.completedAt(matching.getCompletedAt())
					.isCompleted(matching.getStatus() == Matching.Status.COMPLETED)
					.build();
			})
			.collect(Collectors.toList());

		StudentMyPageResponse.StudentInfo studentInfo = StudentMyPageResponse.StudentInfo.builder()
			.updatedAt(student.getUpdatedAt())
			.isAuthenticated(student.getIsAuthenticated())
			.birth(student.getBirth())
			.isEmployment(student.getIsEmployment())
			.phoneCall(student.getPhoneCall())
			.region(student.getRegion())
			.school(student.getSchool())
			.isPublic(student.getIsPublic())
			.temperature(student.getTemperature())
			.createdAt(student.getCreatedAt())
			.career(student.getCareer())
			.id(student.getId())
			.introduction(student.getIntroduction()).build();

		return StudentMyPageResponse.builder()
			.userInfo(userInfo)
			.matchingHistory(matchingHistory)
			.studentInfo(studentInfo)
			.build();
	}

	private String getMatchingStatusInKorean(Matching.Status status) {
		return switch (status) {
			case PENDING -> "대기 중";
			case ACCEPTED -> "매칭 중";
			case REJECTED -> "거절됨";
			case COMPLETED -> "매칭 완료";
			default -> "알 수 없음";
		};
	}

	private String getProjectTitle(Matching matching) {
		if (matching.getProjectRequest() != null) {
			return matching.getProjectRequest().getTitle();
		} else if (matching.getApplication() != null) {
			return matching.getApplication().getTitle();
		}
		return "제목 없음";
	}

	private String getProjectPeriod(Matching matching) {
		if (matching.getProjectRequest() != null) {
			var projectRequest = matching.getProjectRequest();
			if (projectRequest.getStartDate() != null && projectRequest.getEndDate() != null) {
				return projectRequest.getStartDate() + " ~ " + projectRequest.getEndDate();
			} else if (projectRequest.getEstimatedDuration() != null) {
				return projectRequest.getEstimatedDuration() + "일";
			}
		}
		return "기간 미정";
	}

	private int getTotalProjectCount(Store store) {
		// 해당 업체의 모든 매칭 수 (채팅방 기반)
		return matchingService.countByChatRoomStore(store);
	}

	private int getOngoingProjectCount(Store store) {
		// 해당 업체의 진행 중인 매칭 수 (ACCEPTED 상태)
		return matchingService.countByChatRoomStoreAndStatus(store, Matching.Status.ACCEPTED);
	}
}
