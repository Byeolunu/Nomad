package nutar.back.dao.entites;
import nutar.back.dao.enums.MissionStatus;
import nutar.back.dao.enums.MissionType;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import nutar.back.dao.entites.Skill;

@Getter
@Setter
@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    private List<Skill> requiredSkills ;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private MissionStatus status = MissionStatus.OPEN;

    private Double proposedBudget;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private MissionType type; // HOURLY, FIXED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;


    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();

    public Mission() {
        this.createdAt = LocalDateTime.now();
    }
}