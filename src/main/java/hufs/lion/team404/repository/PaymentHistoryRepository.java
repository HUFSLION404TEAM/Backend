package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Payment;
import hufs.lion.team404.domain.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    
    List<PaymentHistory> findByPaymentIdOrderByCreatedAtDesc(Long paymentId);
    
    List<PaymentHistory> findByCurrentStatus(Payment.Status currentStatus);
    
    List<PaymentHistory> findByChangedBy(Long changedBy);
}
