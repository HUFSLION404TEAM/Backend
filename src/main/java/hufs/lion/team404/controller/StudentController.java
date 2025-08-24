package hufs.lion.team404.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import hufs.lion.team404.domain.dto.request.StudentCreateRequestDto;
import hufs.lion.team404.domain.dto.request.StudentSearchRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.PageResponse;
import hufs.lion.team404.domain.dto.response.StudentMyPageResponse;
import hufs.lion.team404.domain.dto.response.StudentProfileResponse;
import hufs.lion.team404.domain.dto.response.StudentResponse;
import hufs.lion.team404.model.StudentModel;
import hufs.lion.team404.model.StudentProfileModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Slf4j

@Tag(name = "학생", description = "학생 관련 API")
public class StudentController {
	private final StudentModel studentModel;
	private final StudentProfileModel studentProfileModel;

	@PostMapping("/create")
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

	@PostMapping("/search")
	@Operation(
		summary = "학생 정보 조회",
		description = "학생 정보를 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<PageResponse<StudentResponse>> search(
		@RequestBody StudentSearchRequestDto request,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return studentModel.search(request, page, size);
	}

	@GetMapping("/mypage")
	@Operation(
		summary = "학생 정보 조회",
		description = "학생 정보를 조회합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<StudentMyPageResponse> getMyPage(
		@AuthenticationPrincipal UserPrincipal authentication) {

		Long userId = authentication.getId();
		StudentMyPageResponse myPageData = studentModel.getMyPage(userId);
		return ApiResponse.success("마이페이지 정보를 성공적으로 조회했습니다.", myPageData);
	}

	@GetMapping("/profile/{studentId}")
	@Operation(
		summary = "학생 프로필 조회 (가게용)",
		description = "가게에서 학생의 상세 프로필을 조회합니다. 이름, 대학교, 전공, 경력, 자기소개, 보유 역량, 포트폴리오, 온도, 매칭후기를 포함합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<StudentProfileResponse> getStudentProfile(
		@org.springframework.web.bind.annotation.PathVariable Long studentId) {
		
		StudentProfileResponse profile = studentProfileModel.getStudentProfile(studentId);
		return ApiResponse.success("학생 프로필을 성공적으로 조회했습니다.", profile);
	}

}
