package nutar.back.dao.repositories;

import nutar.back.dao.entites.PlatformConnection;
import nutar.back.dao.entites.User;
import nutar.back.dao.enums.ConnectionStatus;
import nutar.back.dao.enums.PlatformType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlateformeRepository extends JpaRepository<PlatformConnection, Long> {
    List<PlatformConnection> findByUserIdAndPlatformType(Long userId, PlatformType platformType);
    Optional<PlatformConnection> findByUserIdAndPlatformTypeAndBaseUrl(Long userId, PlatformType platformType, String baseUrl);
    List<PlatformConnection> findByUserIdAndStatus(Long userId, ConnectionStatus status);
}
