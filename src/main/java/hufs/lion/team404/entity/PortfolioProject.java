package hufs.lion.team404.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "portfolio_projects")
@Data
@NoArgsConstructor
public class PortfolioProject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String introduction;
    
    @Column(columnDefinition = "TEXT")
    private String outline;
    
    @Column(columnDefinition = "TEXT")
    private String work;
    
    @Column(columnDefinition = "TEXT")
    private String process;
    
    @Column(columnDefinition = "TEXT")
    private String result;
    
    @Column(columnDefinition = "TEXT")
    private String grow;
    
    @Column(columnDefinition = "TEXT")
    private String competency;
    
    @Column(columnDefinition = "TEXT")
    private String prize;
    
    @Column(nullable = false)
    private Integer displayOrder = 0;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
