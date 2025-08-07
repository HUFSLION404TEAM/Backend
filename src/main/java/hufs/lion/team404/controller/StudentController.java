package hufs.lion.team404.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hufs.lion.team404.domain.dto.request.StudentCreateRequestDto;
import hufs.lion.team404.model.StudentModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class StudentController {
	private final StudentModel studentModel;

	@PostMapping("/")
	public ResponseEntity<?> createStudent(@AuthenticationPrincipal UserPrincipal authentication,
		@RequestBody StudentCreateRequestDto studentCreateRequestDto) {
		Long user_id = authentication.getId();

		studentModel.createStudent(studentCreateRequestDto, user_id);
		return ResponseEntity.ok("success");
	}
}
