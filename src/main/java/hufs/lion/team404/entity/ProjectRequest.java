package hufs.lion.team404.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project_requests")
@Data
@NoArgsConstructor
public class ProjectRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    @Column(nullable = false)
    private String title;
    
    // 프로젝트 개요
    @Column(columnDefinition = "TEXT")
    private String projectOverview;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private Integer estimatedDuration;
    
    // 구체적 업무 내용
    @Column(columnDefinition = "TEXT")
    private String detailedTasks;
    
    @Column(columnDefinition = "TEXT")
    private String requiredSkills;
    
    // 프로젝트 조건
    private Integer budget;
    
    private String paymentMethod;
    
    private String workLocation;
    
    private String workSchedule;
    
    // 대학생 요구조건
    private String preferredMajor;
    
    private Integer minGrade;
    
    private String requiredExperience;
    
    // 기타사항
    @Column(columnDefinition = "TEXT")
    private String additionalNotes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 연관관계
    @OneToMany(mappedBy = "projectRequest", cascade = CascadeType.ALL)
    private List<Matching> matchings;
    
    public enum Status {
        ACTIVE, CLOSED, MATCHED
    }
}
