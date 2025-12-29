package nutar.back.service;
import nutar.back.dao.entites.Education;
import nutar.back.dao.repositories.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class EducationManager implements EducationService {
    @Autowired
    private EducationRepository educationRepository;
    @Override
    public Education createEducation(Education education) {
        return educationRepository.save(education);
    }
    @Override
    public Optional<Education> getEducationById(Long id) {
        return educationRepository.findById(id);
    }
    @Override
    public List<Education> getAllEducations() {
        return educationRepository.findAll();
    }
    @Override
    public Education updateEducation(Education education) {
        return educationRepository.save(education);
    }
    @Override
    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }
    @Override
    public List<Education> findByProfileId(Long profileId) {
        return educationRepository.findByProfileId(profileId);
    }
    @Override
    public void deleteByProfileId(Long profileId) {
        educationRepository.deleteByProfileId(profileId);
    }
    @Override
    public List<Education> findByInstitution(String institution) {
        return educationRepository.findByInstitutionContainingIgnoreCase(institution);
    }
}
