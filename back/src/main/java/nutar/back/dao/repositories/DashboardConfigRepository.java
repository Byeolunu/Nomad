package nutar.back.dao.repositories;

import nutar.back.dao.entites.DashboardConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DashboardConfigRepository extends JpaRepository<DashboardConfig, Long> {
    List<DashboardConfig> findByUserId(Long userId);
    Optional<DashboardConfig> findByUserIdAndIsDefault(Long userId, boolean isDefault);
}
