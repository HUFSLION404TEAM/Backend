package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
@Data
@NoArgsConstructor
public class Favorite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "target_student_id")
    private Student targetStudent;
    
    @ManyToOne
    @JoinColumn(name = "target_project_request_id")
    private ProjectRequest targetProjectRequest;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FavoriteType favoriteType;
    
    @Column(columnDefinition = "TEXT")
    private String memo;
    
    @Column(nullable = false)
    private Boolean isNotificationEnabled = false;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum FavoriteType {
        STUDENT, PROJECT_REQUEST
    }
}
