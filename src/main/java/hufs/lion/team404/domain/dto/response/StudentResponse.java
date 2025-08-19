package hufs.lion.team404.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private Boolean isAuthenticated;
    private String name;
    private String birth;
    private String phoneCall;
    private String email;
    private String school;
    private String introduction;
    private String career;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isPublic;
    private Boolean isEmployment;
    private String region;

    @Builder
    public StudentResponse(Long id, Boolean isAuthenticated, String name, String birth, String phoneCall, String email,
                           String school, String introduction, String career, LocalDateTime createdAt, LocalDateTime updatedAt,
                           Boolean isPublic, Boolean isEmployment, String region) {
        this.id = id;
        this.isAuthenticated = isAuthenticated;
        this.name = name;
        this.birth = birth;
        this.phoneCall = phoneCall;
        this.email = email;
        this.school = school;
        this.introduction = introduction;
        this.career = career;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isPublic = isPublic;
        this.isEmployment = isEmployment;
        this.region = region;
    }
}
