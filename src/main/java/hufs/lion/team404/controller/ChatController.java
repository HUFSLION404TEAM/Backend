package hufs.lion.team404.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.model.ChatModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "채팅", description = "채팅 관련 API")
public class ChatController {

	private final ChatModel chatModel;

	@PostMapping("/create")
	@Operation(
		summary = "채팅방 생성",
		description = "현재 사용자와 채팅방을 생성해줍니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> createRoom(@AuthenticationPrincipal UserPrincipal authentication,
		@RequestParam Long target_id) {
		Long userId = authentication.getId();
		chatModel.createChatRoom(userId, target_id);
		return ApiResponse.success("채팅방을 성공적으로 생성하였습니다.");
	}
	@GetMapping({"/rooms", "/ws/chat/rooms"})
	@Operation(
		summary = "내 채팅방 목록 조회",
		description = "로그인한 사용자의 역할(학생/업체)에 따라 본인이 속한 채팅방 리스트를 최신순으로 반환합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<?> getMyChatRooms(@AuthenticationPrincipal UserPrincipal authentication) {
		Long userId = authentication.getId();
		return ApiResponse.success(chatModel.getMyChatRooms(userId));
	}
}
