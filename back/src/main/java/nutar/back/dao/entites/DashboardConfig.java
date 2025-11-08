package nutar.back.dao.entites;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "dashboard_configs")
public class DashboardConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String widgetLayout;

    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "dashboardConfig", cascade = CascadeType.ALL)
    private List<WidgetConfig> widgetConfigs;
}