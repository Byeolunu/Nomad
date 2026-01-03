package nutar.back.service;
import nutar.back.dao.entites.Education;
import java.util.List;
import java.util.Optional;
public interface EducationService {
    Education createEducation(Education education);
    Optional<Education> getEducationById(Long id);
    List<Education> getAllEducations();
    Education updateEducation(Education education);
    void deleteEducation(Long id);
    List<Education> findByProfileId(Long profileId);
    void deleteByProfileId(Long profileId);
    List<Education> findByInstitution(String institution);
}
