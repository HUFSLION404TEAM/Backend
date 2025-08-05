package hufs.lion.team404.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "matching_id", nullable = false)
    private Matching matching;
    
    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;
    
    @ManyToOne
    @JoinColumn(name = "payee_id", nullable = false)
    private User payee;
    
    // 결제 금액 정보
    @Column(nullable = false)
    private Integer totalAmount;
    
    @Column(nullable = false)
    private Integer platformFee;
    
    @Column(nullable = false)
    private Integer actualAmount;
    
    @Column(nullable = false)
    private String currency = "KRW";
    
    // 결제 방법 및 정보
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    
    private String paymentKey;
    
    private String transactionId;
    
    // 결제 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String failureReason;
    
    // 은행 계좌 정보 (학생 수취용)
    private String payeeBankCode;
    
    private String payeeBankName;
    
    private String payeeAccountNumber;
    
    private String payeeAccountHolder;
    
    // 결제 일정
    private LocalDateTime paymentDueDate;
    
    private LocalDateTime paymentRequestedAt;
    
    private LocalDateTime paymentCompletedAt;
    
    private LocalDateTime transferCompletedAt;
    
    // 메타데이터
    @Column(columnDefinition = "JSON")
    private String paymentMetadata;
    
    @Column(columnDefinition = "TEXT")
    private String adminMemo;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 연관관계
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<PaymentHistory> paymentHistories;
    
    public enum PaymentMethod {
        BANK_TRANSFER, TOSS, KAKAO_PAY, NAVER_PAY, CARD
    }
    
    public enum Status {
        PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, REFUNDED
    }
}
