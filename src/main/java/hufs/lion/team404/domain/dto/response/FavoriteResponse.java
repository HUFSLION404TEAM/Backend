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
    
    @Schema(description = "찜 타입", example = "STUDENT_USER")
    private String favoriteType;
    
    @Schema(description = "대상 학생 유저 정보 (학생을 찜한 경우)")
    private StudentUserInfo studentUserInfo;
    
    @Schema(description = "대상 구인글 정보 (구인글을 찜한 경우)")
    private RecruitingInfo recruitingInfo;
    
    @Schema(description = "메모", example = "관심있는 학생입니다")
    private String memo;
    
    @Schema(description = "알림 활성화 여부", example = "true")
    private Boolean isNotificationEnabled;
    
    @Schema(description = "찜한 날짜", example = "2024-01-01T12:00:00")
    private LocalDateTime createdAt;
    
    @Data
    @Builder
    @Schema(description = "학생 유저 정보")
    public static class StudentUserInfo {
        
        @Schema(description = "유저 ID", example = "1")
        private Long userId;
        
        @Schema(description = "학생 이름", example = "홍길동")
        private String name;
        
        @Schema(description = "학생 이메일", example = "student@example.com")
        private String email;
        
        @Schema(description = "학교", example = "한국외국어대학교")
        private String university;
        
        @Schema(description = "전공", example = "컴퓨터공학과")
        private String major;
        
        @Schema(description = "프로필 이미지 URL")
        private String profileImage;
        
        @Schema(description = "온도", example = "36.5")
        private Double temperature;
    }
    
    @Data
    @Builder
    @Schema(description = "구인글 정보")
    public static class RecruitingInfo {
        
        @Schema(description = "구인글 ID", example = "1")
        private Long recruitingId;
        
        @Schema(description = "구인글 제목", example = "웹사이트 개발 프로젝트")
        private String title;
        
        @Schema(description = "진행 기간", example = "30일")
        private String progressPeriod;
        
        @Schema(description = "가격", example = "500,000원")
        private String price;
        
        @Schema(description = "업체명", example = "홍길동 카페")
        private String storeName;
        
        @Schema(description = "업체 카테고리", example = "카페")
        private String storeCategory;
        
        @Schema(description = "현재 모집중 여부", example = "true")
        private Boolean isRecruiting;
    }
    
    public static FavoriteResponse fromEntity(Favorite favorite) {
        FavoriteResponseBuilder builder = FavoriteResponse.builder()
                .favoriteId(favorite.getId())
                .favoriteType(favorite.getFavoriteType().name())
                .memo(favorite.getMemo())
                .isNotificationEnabled(favorite.getIsNotificationEnabled())
                .createdAt(favorite.getCreatedAt());
        
        // 학생 유저를 찜한 경우
        if (favorite.getTargetStudentUser() != null && favorite.getTargetStudentUser().getStudent() != null) {
            builder.studentUserInfo(StudentUserInfo.builder()
                    .userId(favorite.getTargetStudentUser().getId())
                    .name(favorite.getTargetStudentUser().getName())
                    .email(favorite.getTargetStudentUser().getEmail())
                    .university(favorite.getTargetStudentUser().getStudent().getSchool())
                    .major(favorite.getTargetStudentUser().getStudent().getMajor())
                    .profileImage(favorite.getTargetStudentUser().getProfileImage())
                    .temperature(favorite.getTargetStudentUser().getStudent().getTemperature())
                    .build());
        }
        
        // 구인글을 찜한 경우
        if (favorite.getTargetRecruiting() != null) {
            builder.recruitingInfo(RecruitingInfo.builder()
                    .recruitingId(favorite.getTargetRecruiting().getId())
                    .title(favorite.getTargetRecruiting().getTitle())
                    .progressPeriod(favorite.getTargetRecruiting().getProgressPeriod())
                    .price(favorite.getTargetRecruiting().getPrice())
                    .storeName(favorite.getTargetRecruiting().getStore() != null ? 
                        favorite.getTargetRecruiting().getStore().getStoreName() : null)
                    .storeCategory(favorite.getTargetRecruiting().getStore() != null ? 
                        favorite.getTargetRecruiting().getStore().getCategory() : null)
                    .isRecruiting(isCurrentlyRecruiting(favorite.getTargetRecruiting().getRecruitmentPeriod()))
                    .build());
        }
        
        return builder.build();
    }
    
    /**
     * 모집 기간을 파싱하여 현재 모집중인지 확인
     */
    private static boolean isCurrentlyRecruiting(String recruitmentPeriod) {
        try {
            if (recruitmentPeriod == null || !recruitmentPeriod.contains("~")) {
                return true;
            }
            
            String[] dates = recruitmentPeriod.split("~");
            if (dates.length != 2) {
                return true;
            }
            
            String endDateStr = dates[1].trim();
            java.time.LocalDate endDate = java.time.LocalDate.parse(endDateStr, 
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            java.time.LocalDate today = java.time.LocalDate.now();
            
            return !today.isAfter(endDate);
        } catch (Exception e) {
            return true;
        }
    }
}
