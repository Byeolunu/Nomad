package nutar.back.service;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.Mission;
import java.util.List;
public interface RecruiterService {
    Recruiter createRecruiter(Recruiter recruiter);
    Recruiter getRecruiterById(Long id);
    List<Recruiter> getAllRecruiters();
    Recruiter updateRecruiter(Recruiter recruiter);
    void deleteRecruiter(Long id);
    List<Recruiter> findByCompanyName(String companyName);
    List<Mission> getPostedMissions(Long recruiterId);
    Recruiter getRecruiterByEmail(String email);
}
