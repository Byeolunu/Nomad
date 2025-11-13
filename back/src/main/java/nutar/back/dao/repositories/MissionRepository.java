package nutar.back.dao.repositories;

import nutar.back.dao.entites.Mission;
import nutar.back.dao.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface MissionRepository extends JpaRepository<Mission, Long>
{
    List<Mission> findByStatus(MissionStatus status);
    List<Mission> findByRecruiterId(Long recruiterId);
    List<Mission> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    List<Mission> findByRequiredSkillsContaining(String skill);
    List<Mission> findByStatusAndRecruiterId(MissionStatus status, Long recruiterId);
}
