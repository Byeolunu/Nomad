package nutar.back.service;
import nutar.back.dao.entites.Application;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Mission;
import nutar.back.dao.enums.ApplicationStatus;
import nutar.back.dao.repositories.ApplicationRepository;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.dao.repositories.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ApplicationManager implements ApplicationService
{
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private FreelancerRepository freelancerRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Override
    public Application applyToMission(Application application) {
        application.setStatus(ApplicationStatus.PENDING);
        return applicationRepository.save(application);
    }
    @Override
    public List<Application> getApplicationsForMission(Long missionId) {
        return applicationRepository.findByMissionId(missionId);
    }
    @Override
    public List<Application> getFreelancerApplications(Long freelancerId) {
        return applicationRepository.findByFreelancerId(freelancerId);
    }
    @Override
    public Application updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        ApplicationStatus newStatus = ApplicationStatus.valueOf(status.toUpperCase());
        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
}
