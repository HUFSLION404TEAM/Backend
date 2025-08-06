package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByMatchingId(Long matchingId);
    
    List<Payment> findByPayerId(Long payerId);
    
    List<Payment> findByPayeeId(Long payeeId);
    
    List<Payment> findByStatus(Payment.Status status);
    
    Optional<Payment> findByPaymentKey(String paymentKey);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByStatusOrderByCreatedAtDesc(Payment.Status status);
}
