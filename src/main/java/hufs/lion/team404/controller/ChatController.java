package hufs.lion.team404.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
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
}
