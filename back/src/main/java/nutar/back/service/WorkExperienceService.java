package nutar.back.service;
import nutar.back.dao.entites.WorkExperience;
import java.util.List;
import java.util.Optional;
public interface WorkExperienceService {
    WorkExperience createWorkExperience(WorkExperience workExperience);
    Optional<WorkExperience> getWorkExperienceById(Long id);
    List<WorkExperience> getAllWorkExperiences();
    WorkExperience updateWorkExperience(WorkExperience workExperience);
    void deleteWorkExperience(Long id);
    List<WorkExperience> findByProfileId(Long profileId);
    List<WorkExperience> findByFreelancerId(Long freelancerId);
    void deleteByProfileId(Long profileId);
    List<WorkExperience> findByCompanyName(String companyName);
}
