package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Favorite;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "찜 정보 응답 DTO")
public class FavoriteResponse {
    
    @Schema(description = "찜 ID", example = "1")
    private Long favoriteId;
    
    @Schema(description = "찜 타입", example = "STUDENT")
    private String favoriteType;
    
    @Schema(description = "대상 학생 정보 (학생을 찜한 경우)")
    private StudentInfo studentInfo;
    
    @Schema(description = "대상 가게 정보 (가게를 찜한 경우)")
    private StoreInfo storeInfo;
    
    @Schema(description = "메모", example = "관심있는 학생입니다")
    private String memo;
    
    @Schema(description = "알림 활성화 여부", example = "true")
    private Boolean isNotificationEnabled;
    
    @Schema(description = "찜한 날짜", example = "2024-01-01T12:00:00")
    private LocalDateTime createdAt;
    
    @Data
    @Builder
    @Schema(description = "학생 정보")
    public static class StudentInfo {
        
        @Schema(description = "학생 ID", example = "1")
        private Long studentId;
        
        @Schema(description = "학생 이름", example = "홍길동")
        private String name;
        
        @Schema(description = "학교", example = "한국외국어대학교")
        private String university;
        
        @Schema(description = "전공", example = "컴퓨터공학과")
        private String major;
        
        @Schema(description = "프로필 이미지 URL")
        private String profileImage;
        
        @Schema(description = "학년", example = "3")
        private Integer grade;
    }
    
    @Data
    @Builder
    @Schema(description = "가게 정보")
    public static class StoreInfo {
        
        @Schema(description = "가게 ID", example = "1")
        private Long storeId;
        
        @Schema(description = "가게명", example = "테크스타트업")
        private String storeName;
        
        @Schema(description = "업종", example = "IT")
        private String industry;
        
        @Schema(description = "위치", example = "서울특별시 강남구")
        private String location;
        
        @Schema(description = "로고 이미지 URL")
        private String logoImage;
        
        @Schema(description = "평점", example = "4.5")
        private Double rating;
    }
    
    public static FavoriteResponse fromEntity(Favorite favorite) {
        FavoriteResponseBuilder builder = FavoriteResponse.builder()
                .favoriteId(favorite.getId())
                .favoriteType(favorite.getFavoriteType().name())
                .memo(favorite.getMemo())
                .isNotificationEnabled(favorite.getIsNotificationEnabled())
                .createdAt(favorite.getCreatedAt());
        
        if (favorite.getTargetStudent() != null) {
            builder.studentInfo(StudentInfo.builder()
                    .studentId(favorite.getTargetStudent().getId())
                    .name(favorite.getTargetStudent().getUser().getName())
                    .university(favorite.getTargetStudent().getSchool())
                    .major(favorite.getTargetStudent().getMajor())
                    .profileImage(favorite.getTargetStudent().getUser().getProfileImage())
                    .grade(null) // Student 엔티티에 grade 필드가 없으므로 null로 설정
                    .build());
        }
        
        if (favorite.getTargetStore() != null) {
            builder.storeInfo(StoreInfo.builder()
                    .storeId(Long.parseLong(favorite.getTargetStore().getBusinessNumber()))
                    .storeName(favorite.getTargetStore().getStoreName())
                    .industry(favorite.getTargetStore().getCategory())
                    .location(favorite.getTargetStore().getAddress())
                    .logoImage(null) // Store 엔티티에 logoImage 필드가 없으므로 null로 설정
                    .rating(null) // Store 엔티티에 rating 필드가 없으므로 null로 설정
                    .build());
        }
        
        return builder.build();
    }
}
