package nutar.back.service;

import nutar.back.dao.entites.Mission;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.repositories.MissionRepository;
import nutar.back.dao.repositories.RecruiterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecruiterManager implements RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final MissionRepository missionRepository;

    public RecruiterManager(RecruiterRepository recruiterRepository, MissionRepository missionRepository) {
        this.recruiterRepository = recruiterRepository;
        this.missionRepository = missionRepository;
    }

    @Override
    public Recruiter createRecruiter(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    @Override
    public Recruiter getRecruiterById(Long id) {
        return recruiterRepository.findById(id).orElse(null);
    }

    @Override
    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    @Override
    public Recruiter updateRecruiter(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    @Override
    public void deleteRecruiter(Long id) {
        recruiterRepository.deleteById(id);
    }

    @Override
    public List<Recruiter> findByCompanyName(String companyName) {
        return recruiterRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

    @Override
    public List<Mission> getPostedMissions(Long recruiterId) {
        return missionRepository.findByRecruiterId(recruiterId);
    }

    @Override
    public Recruiter getRecruiterByEmail(String email) {
        return recruiterRepository.findByEmail(email).orElse(null);
    }
}
