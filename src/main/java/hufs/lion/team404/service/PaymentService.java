package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Payment;
import hufs.lion.team404.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    @Transactional
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }
    
    public List<Payment> findByMatchingId(Long matchingId) {
        return paymentRepository.findByMatchingId(matchingId);
    }
    
    public List<Payment> findByPayerId(Long payerId) {
        return paymentRepository.findByPayerId(payerId);
    }
    
    public List<Payment> findByPayeeId(Long payeeId) {
        return paymentRepository.findByPayeeId(payeeId);
    }
    
    public List<Payment> findByStatus(Payment.Status status) {
        return paymentRepository.findByStatus(status);
    }
    
    public Optional<Payment> findByPaymentKey(String paymentKey) {
        return paymentRepository.findByPaymentKey(paymentKey);
    }
    
    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }
    
    public List<Payment> findByStatusOrderByCreatedAtDesc(Payment.Status status) {
        return paymentRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }
}
