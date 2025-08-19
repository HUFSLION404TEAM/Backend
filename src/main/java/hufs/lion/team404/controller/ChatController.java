package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ChatRoomResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

	@GetMapping({"/student"})
	@Operation(
		summary = "내 채팅방 목록 조회",
		description = "로그인한 사용자의 역할 학생에 따라 본인이 속한 채팅방 리스트를 최신순으로 반환합니다.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<List<ChatRoomResponse>> getMyChatRooms(@AuthenticationPrincipal UserPrincipal authentication) {
		Long userId = authentication.getId();
		List<ChatRoomResponse> chatRooms = chatModel.getMyChatRooms(userId);
		return ApiResponse.success("채팅방 목록을 성공적으로 조회했습니다.", chatRooms);
	}

	@GetMapping("/store/{businessNumber}")
	@Operation(
			summary = "업체 채팅방 조회",
			description = "업체의 채팅방 리스트를 조회합니다.",
			security = @SecurityRequirement(name = "Bearer Authentication")
	)
	public ApiResponse<List<ChatRoomResponse>> getStoreChatRoomList(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PathVariable String businessNumber) {

		Long userId = userPrincipal.getId();
		List<ChatRoomResponse> items = chatModel.getStoreChatRoomList(userId, businessNumber);
		return ApiResponse.success("채팅방을 성공적으로 조회하였습니다.", items);
	}

}
