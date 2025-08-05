package hufs.lion.team404.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Data
@NoArgsConstructor
public class ChatRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "store_user_id", nullable = false)
    private User storeUser;
    
    @ManyToOne
    @JoinColumn(name = "student_user_id", nullable = false)
    private User studentUser;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InitiatedBy initiatedBy;
    
    private LocalDateTime lastMessageAt;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 연관관계
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;
    
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Matching> matchings;
    
    public enum InitiatedBy {
        STORE, STUDENT
    }
}
