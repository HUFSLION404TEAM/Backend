package hufs.lion.team404.service;

import hufs.lion.team404.entity.PaymentHistory;
import hufs.lion.team404.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentHistoryService {
    
    private final PaymentHistoryRepository paymentHistoryRepository;
    
    @Transactional
    public PaymentHistory save(PaymentHistory paymentHistory) {
        return paymentHistoryRepository.save(paymentHistory);
    }
    
    public Optional<PaymentHistory> findById(Long id) {
        return paymentHistoryRepository.findById(id);
    }
    
    public List<PaymentHistory> findByPaymentIdOrderByCreatedAtDesc(Long paymentId) {
        return paymentHistoryRepository.findByPaymentIdOrderByCreatedAtDesc(paymentId);
    }
    
    public List<PaymentHistory> findByCurrentStatus(hufs.lion.team404.entity.Payment.Status currentStatus) {
        return paymentHistoryRepository.findByCurrentStatus(currentStatus);
    }
    
    public List<PaymentHistory> findByChangedBy(Long changedBy) {
        return paymentHistoryRepository.findByChangedBy(changedBy);
    }
    
    public List<PaymentHistory> findAll() {
        return paymentHistoryRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        paymentHistoryRepository.deleteById(id);
    }
}
