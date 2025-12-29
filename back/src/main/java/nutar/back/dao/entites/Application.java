package nutar.back.dao.entites;
import lombok.Getter;
import lombok.Setter;
import nutar.back.dao.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private Freelancer freelancer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;
    @Column(length = 2000)
    private String coverLetter;
    private Double proposedBudget;
    private LocalDateTime appliedDate;
    private LocalDateTime updatedDate;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private Integer estimatedDays;
    private String additionalNotes;
    public Application() {
        this.appliedDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
}
