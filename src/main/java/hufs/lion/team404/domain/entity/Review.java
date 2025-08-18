package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "matching_id", nullable = false)
    private Matching matching;
    
    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewerType reviewerType;
    
    @Column(nullable = false)
    private Integer rating;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum ReviewerType {
        STORE, STUDENT
    }
    
    /**
     * 매칭에서 리뷰 대상자를 가져오는 메서드
     */
    public User getReviewee() {
        if (matching == null || matching.getChatRoom() == null) {
            return null;
        }
        
        User student = matching.getChatRoom().getStudent().getUser();
        User store = matching.getChatRoom().getStore().getUser();
        
        // 리뷰어가 아닌 사람이 리뷰 대상자
        if (reviewer.getId().equals(student.getId())) {
            return store;
        } else if (reviewer.getId().equals(store.getId())) {
            return student;
        }
        
        return null;
    }
}
