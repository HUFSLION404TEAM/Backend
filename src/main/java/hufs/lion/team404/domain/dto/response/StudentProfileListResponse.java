package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Student;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentProfileListResponse {
    
    private Long studentId;
    private Long userId;
    private String name;
    private String email;
    private String profileImage;
    
    // 학생 기본 정보
    private String school;
    private String major;
    private String region;
    private Boolean isEmployment; // 구직중/휴식중
    private Double temperature;
    
    // 간단한 소개 (목록용)
    private String shortIntroduction; // 소개글 요약 (50자 이내)
    private String capabilities; // 보유 역량
    
    // 포트폴리오 수
    private Long portfolioCount;
    
    // 찜 받은 수
    private Long favoriteCount;
    
    // 가입일
    private LocalDateTime createdAt;
    
    public static StudentProfileListResponse fromEntity(Student student) {
        return fromEntity(student, 0L, 0L); // 기본값으로 0 설정
    }
    
    public static StudentProfileListResponse fromEntity(Student student, Long portfolioCount, Long favoriteCount) {
        return StudentProfileListResponse.builder()
                .studentId(student.getId())
                .userId(student.getUser() != null ? student.getUser().getId() : null)
                .name(student.getUser() != null ? student.getUser().getName() : null)
                .email(student.getUser() != null ? student.getUser().getEmail() : null)
                .profileImage(student.getUser() != null ? student.getUser().getProfileImage() : null)
                .school(student.getSchool())
                .major(student.getMajor())
                .region(student.getRegion())
                .isEmployment(student.getIsEmployment())
                .temperature(student.getTemperature())
                .shortIntroduction(getShortIntroduction(student.getIntroduction()))
                .capabilities(student.getCapabilities())
                .portfolioCount(portfolioCount)
                .favoriteCount(favoriteCount)
                .createdAt(student.getCreatedAt())
                .build();
    }
    
    /**
     * 소개글을 50자 이내로 요약
     */
    private static String getShortIntroduction(String introduction) {
        if (introduction == null || introduction.trim().isEmpty()) {
            return null;
        }
        
        String trimmed = introduction.trim();
        if (trimmed.length() <= 50) {
            return trimmed;
        }
        
        return trimmed.substring(0, 50) + "...";
    }
}
