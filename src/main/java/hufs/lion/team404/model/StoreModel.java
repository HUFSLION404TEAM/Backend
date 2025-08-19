package hufs.lion.team404.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import hufs.lion.team404.domain.dto.request.StoreCreateRequestDto;
import hufs.lion.team404.domain.dto.request.StoreUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.StoreMyPageResponse;
import hufs.lion.team404.domain.dto.response.StoreReadResponseDto;
import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.MatchingService;
import hufs.lion.team404.service.StoreService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreModel {
	private final StoreService storeService;
	private final UserService userService;
	private final MatchingService matchingService;

	public void createStore(StoreCreateRequestDto storeCreateRequestDto, Long user_id) {
		User user = userService.findById(user_id).orElseThrow(() -> new NotFoundException("User not found"));

		// 이미 같은 사업자번호로 등록된 업체가 있는지 확인
		if (storeService.existsByBusinessNumber(storeCreateRequestDto.getBusinessNumber())) {
			throw new CustomException(ErrorCode.STORE_USER_ALREADY_HAVE, "이미 등록된 사업자번호입니다.");
		}

		Store store = Store.builder()
			.businessNumber(storeCreateRequestDto.getBusinessNumber())
			.user(user)
			.storeName(storeCreateRequestDto.getName())
			.address(storeCreateRequestDto.getAddress())
			.contact(storeCreateRequestDto.getPhone())
			.category(storeCreateRequestDto.getType())
			.introduction(storeCreateRequestDto.getIntroduction())
			.build();

		user.setUserRole(UserRole.STORE);

		storeService.save(store);
	}

	public StoreReadResponseDto getStoreByBusinessNumber(String businessNumber) {
		Store store = storeService.findById(businessNumber)
			.orElseThrow(() -> new NotFoundException("Store not found: businessNumber=" + businessNumber));

		return StoreReadResponseDto.fromEntity(store);
	}

	public List<StoreReadResponseDto> getAllStores() {
		return storeService.findAll().stream()
			.map(StoreReadResponseDto::fromEntity)
			.toList();
	}

	public StoreReadResponseDto updateStore(String businessNumber, StoreUpdateRequestDto dto, Long userId) {
		Store updated = storeService.updateStore(businessNumber, dto, userId);
		return StoreReadResponseDto.fromEntity(updated);
	}

	public void deleteStore(String businessNumber, Long userId) {
		storeService.deleteStore(businessNumber, userId);
	}

	// 업체 마이페이지 조회
	@Transactional(readOnly = true)
	public StoreMyPageResponse getMyPage(Long userId) {
		// 사용자 정보 조회
		User user = userService.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		// 사용자의 모든 업체 조회
		List<Store> userStores = storeService.findByUserId(userId);

		// 매칭 이력 조회 (채팅방 기반)
		List<Matching> matchings = matchingService.findByChatRoomStoreUserIdOrderByCreatedAtDesc(userId);

		// 사용자 정보 변환
		StoreMyPageResponse.UserInfo userInfo = StoreMyPageResponse.UserInfo.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.profileImageUrl(user.getProfileImage())
			.joinedAt(user.getCreatedAt())
			.build();

		// 매칭 이력 변환
		List<StoreMyPageResponse.MatchingHistoryInfo> matchingHistory = matchings.stream()
			.map(matching -> {
				String status = getMatchingStatusInKorean(matching.getStatus());
				String projectTitle = getProjectTitle(matching);
				String period = getProjectPeriod(matching);

				return StoreMyPageResponse.MatchingHistoryInfo.builder()
					.matchingId(matching.getId())
					.studentName(matching.getChatRoom().getStudent().getUser().getName())
					.projectTitle(projectTitle)
					.status(status)
					.period(period)
					.matchedAt(matching.getOfferedAt())
					.completedAt(matching.getCompletedAt())
					.isCompleted(matching.getStatus() == Matching.Status.COMPLETED)
					.build();
			})
			.collect(Collectors.toList());

		// 업체 정보 변환
		List<StoreMyPageResponse.StoreInfo> storeInfos = userStores.stream()
			.map(store -> {
				int totalProjects = getTotalProjectCount(store);
				int ongoingProjects = getOngoingProjectCount(store);

				return StoreMyPageResponse.StoreInfo.builder()
					.businessNumber(store.getBusinessNumber())
					.storeName(store.getStoreName())
					.address(store.getAddress())
					.phoneNumber(store.getContact())
					.category(store.getCategory())
					.description(store.getIntroduction())
					.temperature(store.getTemperature())
					.createdAt(store.getCreatedAt())
					.totalProjects(totalProjects)
					.ongoingProjects(ongoingProjects)
					.build();
			})
			.collect(Collectors.toList());

		return StoreMyPageResponse.builder()
			.userInfo(userInfo)
			.matchingHistory(matchingHistory)
			.stores(storeInfos)
			.build();
	}

	private String getMatchingStatusInKorean(Matching.Status status) {
		return switch (status) {
			case PENDING -> "대기 중";
			case ACCEPTED -> "매칭 중";
			case REJECTED -> "거절됨";
			case COMPLETED -> "매칭 완료";
			default -> "알 수 없음";
		};
	}

	private String getProjectTitle(Matching matching) {
		if (matching.getProjectRequest() != null) {
			return matching.getProjectRequest().getTitle();
		} else if (matching.getApplication() != null) {
			return matching.getApplication().getTitle();
		}
		return "제목 없음";
	}

	private String getProjectPeriod(Matching matching) {
		if (matching.getProjectRequest() != null) {
			var projectRequest = matching.getProjectRequest();
			if (projectRequest.getStartDate() != null && projectRequest.getEndDate() != null) {
				return projectRequest.getStartDate() + " ~ " + projectRequest.getEndDate();
			} else if (projectRequest.getEstimatedDuration() != null) {
				return projectRequest.getEstimatedDuration() + "일";
			}
		}
		return "기간 미정";
	}

	private int getTotalProjectCount(Store store) {
		// 해당 업체의 모든 매칭 수 (채팅방 기반)
		return matchingService.countByChatRoomStore(store);
	}

	private int getOngoingProjectCount(Store store) {
		// 해당 업체의 진행 중인 매칭 수 (ACCEPTED 상태)
		return matchingService.countByChatRoomStoreAndStatus(store, Matching.Status.ACCEPTED);
	}

	public StoreReadResponseDto getStoreDetail(String businessNumber) {
		Store store = storeService.findByBusinessNumber(businessNumber)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
		return StoreReadResponseDto.fromEntity(store);
	}
}