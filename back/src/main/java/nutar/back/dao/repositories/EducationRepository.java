package nutar.back.dao.repositories;

import nutar.back.dao.entites.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EducationRepository extends JpaRepository<Education, Long>
{
    List<Education> findByProfileId(Long profileId);
    void deleteByProfileId(Long profileId);
    List<Education> findByInstitutionContainingIgnoreCase(String institution);
}
