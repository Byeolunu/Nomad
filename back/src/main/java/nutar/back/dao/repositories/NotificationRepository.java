package nutar.back.dao.repositories;

import nutar.back.dao.entites.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
