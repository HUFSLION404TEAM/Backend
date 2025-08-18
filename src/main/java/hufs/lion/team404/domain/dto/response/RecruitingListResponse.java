package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Recruiting;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class RecruitingListResponse {
    
    private Long id;
    private String title;
    private String progressPeriod;
    private boolean isRecruiting;
    private String storeName;
    private String storeCategory;
    
    public static RecruitingListResponse fromEntity(Recruiting recruiting) {
        return RecruitingListResponse.builder()
                .id(recruiting.getId())
                .title(recruiting.getTitle())
                .progressPeriod(recruiting.getProgressPeriod())
                .isRecruiting(isCurrentlyRecruiting(recruiting.getRecruitmentPeriod()))
                .storeName(recruiting.getStore().getUser().getName()) // User의 name
                .storeCategory(recruiting.getStore().getCategory())
                .build();
    }
    
    /**
     * 모집 기간을 파싱하여 현재 모집중인지 확인
     */
    private static boolean isCurrentlyRecruiting(String recruitmentPeriod) {
        try {
            if (recruitmentPeriod == null || !recruitmentPeriod.contains("~")) {
                return true; // 기간이 명시되지 않으면 모집중으로 간주
            }
            
            String[] dates = recruitmentPeriod.split("~");
            if (dates.length != 2) {
                return true;
            }
            
            String endDateStr = dates[1].trim();
            LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate today = LocalDate.now();
            
            return !today.isAfter(endDate); // 오늘이 마감일 이후가 아니면 모집중
        } catch (Exception e) {
            return true; // 파싱 오류시 모집중으로 간주
        }
    }
}
