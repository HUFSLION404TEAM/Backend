package hufs.lion.team404.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "matchings")
@Data
@NoArgsConstructor
public class Matching {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_request_id", nullable = false)
    private ProjectRequest projectRequest;
    
    @ManyToOne
    @JoinColumn(name = "application_id")
    private Portfolio portfolio;
    
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchedBy matchedBy;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    
    private LocalDateTime offeredAt;
    
    private LocalDateTime respondedAt;
    
    private LocalDateTime completedAt;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 연관관계
    @OneToMany(mappedBy = "matching", cascade = CascadeType.ALL)
    private List<Review> reviews;
    
    @OneToMany(mappedBy = "matching", cascade = CascadeType.ALL)
    private List<Report> reports;
    
    @OneToMany(mappedBy = "matching", cascade = CascadeType.ALL)
    private List<AiMatchingSummary> aiMatchingSummaries;
    
    @OneToMany(mappedBy = "matching", cascade = CascadeType.ALL)
    private List<Notification> notifications;
    
    @OneToMany(mappedBy = "matching", cascade = CascadeType.ALL)
    private List<Payment> payments;
    
    public enum MatchedBy {
        STORE_OFFER, STUDENT_APPLY
    }
    
    public enum Status {
        PENDING, ACCEPTED, REJECTED, COMPLETED, CANCELLED
    }
    
    public void accept() {
        this.status = Status.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }
    
    public void reject() {
        this.status = Status.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }
    
    public boolean isPending() {
        return this.status == Status.PENDING;
    }
}
