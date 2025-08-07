package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.StudentCreateRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.model.StudentModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "학생", description = "학생 관련 API")
public class StudentController {
	private final StudentModel studentModel;

	@PostMapping("/")
	@Operation(
		summary = "학생 정보 생성", 
		description = "새로운 학생 정보를 생성합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<Void> createStudent(
			@AuthenticationPrincipal UserPrincipal authentication,
			@Valid @RequestBody StudentCreateRequestDto studentCreateRequestDto) {
		
		Long userId = authentication.getId();
		studentModel.createStudent(studentCreateRequestDto, userId);
		
		return ApiResponse.success("학생 정보가 성공적으로 생성되었습니다.");
	}
}
