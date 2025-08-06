package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private Boolean isAuthenticated = false;
    
    private String school;
    
    private LocalDate birth;
    
    private String phoneCall;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 연관관계
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Portfolio> portfolios;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<AiMatchingSummary> aiMatchingSummaries;
}
