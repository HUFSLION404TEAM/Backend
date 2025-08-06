package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private Long referenceId;
    
    private String referenceType;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    private LocalDateTime readAt;
    
    private LocalDateTime pushSentAt;
    
    @ManyToOne
    @JoinColumn(name = "matching_id")
    private Matching matching;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum NotificationType {
        MATCHING_REQUEST, MATCHING_ACCEPTED, MATCHING_REJECTED, MATCHING_COMPLETED
    }
}
