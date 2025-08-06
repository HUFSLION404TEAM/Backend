package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_histories")
@Data
@NoArgsConstructor
public class PaymentHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;
    
    @Enumerated(EnumType.STRING)
    private Payment.Status previousStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Payment.Status currentStatus;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    private Long changedBy;
    
    @Column(columnDefinition = "JSON")
    private String additionalData;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
