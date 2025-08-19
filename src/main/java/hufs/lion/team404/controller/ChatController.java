package hufs.lion.team404.controller;

import hufs.lion.team404.domain.entity.ChatRoom;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.model.ChatModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "채팅", description = "채팅 관련 API")
public class ChatController {

	private final ChatModel chatModel;

	@PostMapping("/create/with-store")
	@Operation(
		summary = "업체와 채팅방 생성",
		description = "학생이 업체와 채팅방을 생성합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> createRoomWithStore(
		@AuthenticationPrincipal UserPrincipal authentication,
		@RequestParam String businessNumber) {
		
		Long userId = authentication.getId();
		chatModel.createChatRoomWithStore(userId, businessNumber);
		return ApiResponse.success("채팅방을 성공적으로 생성하였습니다.");
	}

	@PostMapping("/create/with-student")
	@Operation(
		summary = "학생과 채팅방 생성",
		description = "업체가 학생과 채팅방을 생성합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> createRoomWithStudent(
		@AuthenticationPrincipal UserPrincipal authentication,
		@RequestParam Long studentId,
		@RequestParam String businessNumber) {
		
		Long userId = authentication.getId();
		chatModel.createChatRoomWithStudent(userId, studentId, businessNumber);
		return ApiResponse.success("채팅방을 성공적으로 생성하였습니다.");
	}

	@GetMapping("/{businessNumber}")
	@Operation(
			summary = "업체 채팅방 조회",
			description = "업체의 채팅방 리스트를 조회합니다.",
			security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<List<ChatRoom>> getStoreChatRoomList(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PathVariable String businessNumber) {

		Long userId = userPrincipal.getId();
		List<ChatRoom> items = chatModel.getStoreChatRoomList(userId, businessNumber);
		return ApiResponse.success("채팅방을 성공적으로 조회하였습니다.", items);
	}

}
