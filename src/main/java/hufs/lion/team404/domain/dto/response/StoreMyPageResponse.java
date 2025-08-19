package hufs.lion.team404.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreMyPageResponse {

    // 유저 정보
    private UserInfo userInfo;
    
    // 매칭 이력
    private List<MatchingHistoryInfo> matchingHistory;
    
    // 업체 리스트
    private List<StoreInfo> stores;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String name;
        private String email;
        private String profileImageUrl;
        private LocalDateTime joinedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchingHistoryInfo {
        private Long matchingId;
        private String studentName;
        private String projectTitle;
        private String status; // 매칭중, 매칭완료
        private String period; // 기간 정보
        private LocalDateTime matchedAt;
        private LocalDateTime completedAt;
        private boolean isCompleted;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreInfo {
        private String businessNumber;
        private String storeName;
        private String address;
        private String phoneNumber;
        private String category;
        private String description;
        private Double temperature;
        private LocalDateTime createdAt;
        private int totalProjects;
        private int ongoingProjects;
    }
}
