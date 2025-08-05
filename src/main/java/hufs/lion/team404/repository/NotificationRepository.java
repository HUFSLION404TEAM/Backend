package hufs.lion.team404.repository;

import hufs.lion.team404.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    List<Notification> findByNotificationType(Notification.NotificationType notificationType);
    
    List<Notification> findByMatchingId(Long matchingId);
    
    long countByUserIdAndIsReadFalse(Long userId);
}
