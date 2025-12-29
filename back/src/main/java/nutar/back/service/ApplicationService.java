package nutar.back.service;
import nutar.back.dao.entites.Application;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public interface ApplicationService {
    public Application applyToMission(Application application);
    public List<Application> getApplicationsForMission(Long missionId);
    public List<Application> getFreelancerApplications(Long freelancerId);
    public Application updateApplicationStatus(Long applicationId, String status);
}
