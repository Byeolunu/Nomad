package nutar.back.dao.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nutar.back.dao.enums.WidgetType;

@Getter
@Setter
@Entity
@Table(name = "widget_configs")
public class WidgetConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private WidgetType widgetType;

    private String title;
    private Integer positionX;
    private Integer positionY;
    private Integer width;
    private Integer height;

    @Column(columnDefinition = "TEXT")
    private String filters;
    private Integer refreshInterval; // en secondes

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_config_id")
    private DashboardConfig dashboardConfig;
}