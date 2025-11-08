package nutar.back.dao.entites;

import jakarta.persistence.*;
import lombok.*;
import nutar.back.dao.enums.ConnectionStatus;
import nutar.back.dao.enums.PlatformType;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Entity

@Table(name = "platform_connections")
public class PlatformConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlatformType platformType;

    private String externalUsername;
    private String accessToken;
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;

    private LocalDateTime lastSync;
    private String baseUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(columnDefinition = "TEXT")
    private String platformConfig;
}