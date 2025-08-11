package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chat_rooms")
@Data
@NoArgsConstructor
public class ChatRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
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

    @Builder
    public ChatRoom(Student student, Store store, InitiatedBy initiatedBy) {
        this.store = store;
        this.student = student;
        this.initiatedBy = initiatedBy;
    }

}
