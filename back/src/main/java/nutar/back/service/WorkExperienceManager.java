package nutar.back.service;
import nutar.back.dao.entites.WorkExperience;
import nutar.back.dao.repositories.WorkExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class WorkExperienceManager implements WorkExperienceService {
    @Autowired
    private WorkExperienceRepository workExperienceRepository;
    @Override
    public WorkExperience createWorkExperience(WorkExperience workExperience) {
        return workExperienceRepository.save(workExperience);
    }
    @Override
    public Optional<WorkExperience> getWorkExperienceById(Long id) {
        return workExperienceRepository.findById(id);
    }
    @Override
    public List<WorkExperience> getAllWorkExperiences() {
        return workExperienceRepository.findAll();
    }
    @Override
    public WorkExperience updateWorkExperience(WorkExperience workExperience) {
        return workExperienceRepository.save(workExperience);
    }
    @Override
    public void deleteWorkExperience(Long id) {
        workExperienceRepository.deleteById(id);
    }
    @Override
    public List<WorkExperience> findByProfileId(Long profileId) {
        return workExperienceRepository.findByProfileId(profileId);
    }
    @Override
    public List<WorkExperience> findByFreelancerId(Long freelancerId) {
        return workExperienceRepository.findByProfileFreelancerId(freelancerId);
    }
    @Override
    public void deleteByProfileId(Long profileId) {
        workExperienceRepository.deleteByProfileId(profileId);
    }
    @Override
    public List<WorkExperience> findByCompanyName(String companyName) {
        return workExperienceRepository.findByCompanyContainingIgnoreCase(companyName);
    }
}
