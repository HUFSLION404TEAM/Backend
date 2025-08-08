package hufs.lion.team404.model;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.domain.dto.request.StudentCreateRequestDto;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.repository.StudentRepository;
import hufs.lion.team404.service.StudentService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentModel {
	private final StudentService studentService;
	private final UserService userService;

	public void createStudent(StudentCreateRequestDto studentCreateRequestDto, Long user_id) {
		User user = userService.findById(user_id).orElseThrow(() -> new NotFoundException("User not found"));

		Student student = Student.builder()
			.birth(studentCreateRequestDto.getBirth())
			.career(studentCreateRequestDto.getCareer())
			.introduction(studentCreateRequestDto.getIntroduction())
			.isAuthenticated(false)
			.phoneCall(studentCreateRequestDto.getPhone())
			.school(studentCreateRequestDto.getUniversity())
			.user(user)
			.build();

		user.setUserRole(UserRole.STUDENT);
		studentService.save(student);
	}
}
