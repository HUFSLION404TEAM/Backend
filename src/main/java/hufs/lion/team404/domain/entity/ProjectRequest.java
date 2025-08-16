package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "projectRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<ProjectRequestFile> files = new ArrayList<>();

    public enum Status {
        ACTIVE, CLOSED, MATCHED
    }

    @Builder
    public ProjectRequest(Store store, String title, String projectOverview, LocalDate startDate, LocalDate endDate, Integer estimatedDuration,
                          String detailedTasks, String requiredSkills,
                          Integer budget, String paymentMethod, String workLocation, String workSchedule,
                          String preferredMajor, Integer minGrade, String requiredExperience,
                          String additionalNotes, ProjectRequest.Status status, List<Matching> matchings, List<ProjectRequestFile> files) {

        this.store = Objects.requireNonNull(store, "store must not be null");
        this.title = title;
        this.projectOverview = projectOverview;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimatedDuration = estimatedDuration;
        this.detailedTasks = detailedTasks;
        this.requiredSkills = requiredSkills;
        this.budget = budget;
        this.paymentMethod = paymentMethod;
        this.workLocation = workLocation;
        this.workSchedule = workSchedule;
        this.preferredMajor = preferredMajor;
        this.minGrade = minGrade;
        this.requiredExperience = requiredExperience;
        this.additionalNotes = additionalNotes;
        this.status = status != null ? status : Status.ACTIVE;
        this.matchings = matchings != null ? matchings : new ArrayList<>();
        this.files = files != null ? files : new ArrayList<>();

    }
}
