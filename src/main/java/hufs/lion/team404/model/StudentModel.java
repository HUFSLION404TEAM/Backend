package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.request.StudentSearchRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.PageResponse;
import hufs.lion.team404.domain.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.request.StudentCreateRequestDto;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.StudentService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentModel {
	private final StudentService studentService;
	private final UserService userService;

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

}
