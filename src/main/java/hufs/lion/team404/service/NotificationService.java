package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Notification;
import hufs.lion.team404.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    @Transactional
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
    
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }
    
    public List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }
    
    public List<Notification> findByNotificationType(Notification.NotificationType notificationType) {
        return notificationRepository.findByNotificationType(notificationType);
    }
    
    public List<Notification> findByMatchingId(Long matchingId) {
        return notificationRepository.findByMatchingId(matchingId);
    }
    
    public long countByUserIdAndIsReadFalse(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
    
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }
}
