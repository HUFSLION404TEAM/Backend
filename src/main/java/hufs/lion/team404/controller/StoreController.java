package hufs.lion.team404.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.model.StoreModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class StoreController {
	private final StoreModel storeModel;

	@PostMapping("/")
	public ResponseEntity<?> createStore(@AuthenticationPrincipal UserPrincipal authentication,
		@RequestBody StoreCreateRequestDto storeCreateRequestDto) {
		Long user_id = authentication.getId();

		storeModel.createStore(storeCreateRequestDto, user_id);
		return ResponseEntity.ok("success");
	}



}
