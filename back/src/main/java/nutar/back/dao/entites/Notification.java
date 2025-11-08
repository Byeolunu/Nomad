package nutar.back.dao.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nutar.back.dao.enums.NotificationPriority;
import nutar.back.dao.enums.NotificationType;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // INFO, WARNING, ERROR, SUCCESS

    @Enumerated(EnumType.STRING)
    private NotificationPriority priority; // LOW, MEDIUM, HIGH, URGENT

    private boolean isRead;
    private LocalDateTime createdAt;

    private String sourcePlatform; // JIRA, GITHUB, etc.
    private String sourceUrl; // Lien vers l'élément source

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}