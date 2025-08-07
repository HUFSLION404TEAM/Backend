package hufs.lion.team404.model;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.response.UserResponse;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserModel {

	private final UserService userService;

	public UserResponse getMyInfo(Long id) {
		User user = userService.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
		return UserResponse.builder()
			.userRole(user.getUserRole())
			.name(user.getName())
			.email(user.getEmail())
			.socialProvider(user.getSocialProvider())
			.temperature(user.getTemperature())
			.updatedAt(user.getUpdatedAt())
			.createdAt(user.getCreatedAt())
			.profileImage(user.getProfileImage())
			.socialId(user.getSocialId())
			.id(user.getId())
			.build();
	}
}
