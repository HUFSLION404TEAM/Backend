package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
public class Report {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "matching_id", nullable = false)
    private Matching matching;
    
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReporterType reporterType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String adminResponse;
    
    private Long handledBy;
    
    private LocalDateTime resolvedAt;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum ReporterType {
        STORE, STUDENT
    }
    
    public enum ReportType {
        NO_SHOW, INAPPROPRIATE_BEHAVIOR, PAYMENT_ISSUE, HARASSMENT, SPAM, FRAUD, OTHER
    }
    
    public enum Status {
        PENDING, UNDER_REVIEW, RESOLVED, DISMISSED, ESCALATED
    }
    
    /**
     * 매칭에서 신고 대상자를 가져오는 메서드
     */
    public User getReported() {
        if (matching == null || matching.getChatRoom() == null) {
            return null;
        }
        
        User student = matching.getChatRoom().getStudent().getUser();
        User store = matching.getChatRoom().getStore().getUser();
        
        // 신고자가 아닌 사람이 신고 대상자
        if (reporter.getId().equals(student.getId())) {
            return store;
        } else if (reporter.getId().equals(store.getId())) {
            return student;
        }
        
        return null;
    }
}
