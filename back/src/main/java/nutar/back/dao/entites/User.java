package nutar.back.dao.entites;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String timezone; // Pour les rappels planifiés
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private UserRole role; // ADMIN, USER, etc.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlatformConnection> platformConnections;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DashboardConfig> dashboardConfigs;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}