package hufs.lion.team404.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stores")
@Data
@NoArgsConstructor
public class Store {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String storeName;
    
    @Column(unique = true, nullable = false)
    private String businessNumber;
    
    private String address;
    
    private String contact;
    
    private String category;
    
    @Column(columnDefinition = "TEXT")
    private String introduction;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 연관관계
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<ProjectRequest> projectRequests;
}
