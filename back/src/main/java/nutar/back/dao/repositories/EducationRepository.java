package nutar.back.dao.repositories;

import nutar.back.dao.entites.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long>
{
    List<Education> findByProfileId(Long profileId);
    void deleteByProfileId(Long profileId);
    List<Education> findByInstitutionContainingIgnoreCase(String institution);
}