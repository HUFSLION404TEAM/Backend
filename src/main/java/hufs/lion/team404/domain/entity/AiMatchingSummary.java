package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_matching_summaries")
@Data
@NoArgsConstructor
public class AiMatchingSummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "matching_id", nullable = false)
    private Matching matching;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    // 매칭 요약 정보
    private String storeName;
    
    @Column(columnDefinition = "TEXT")
    private String projectRequirements;
    
    private String projectPeriod;
    
    // AI 분석 결과
    @Column(columnDefinition = "TEXT")
    private String achievements;
    
    @Column(columnDefinition = "TEXT")
    private String analysisSummary;
    
    @Column(columnDefinition = "TEXT")
    private String strengths;
    
    @Column(columnDefinition = "TEXT")
    private String improvements;
    
    @Column(columnDefinition = "TEXT")
    private String recommendations;
    
    // 메타데이터
    private String aiModelVersion;
    
    @Column(columnDefinition = "JSON")
    private String analysisMetadata;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenerationStatus generationStatus = GenerationStatus.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    private LocalDateTime generatedAt;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum GenerationStatus {
        PENDING, GENERATING, COMPLETED, FAILED
    }
}
