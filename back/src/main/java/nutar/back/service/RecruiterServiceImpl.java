package nutar.back.service;

import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.Mission;
import nutar.back.dao.repositories.RecruiterRepository;
import nutar.back.dao.repositories.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private MissionRepository missionRepository;

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
        return recruiterRepository.findByCompanyName(companyName);
    }

    @Override
    public List<Mission> getPostedMissions(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElse(null);
        if (recruiter != null) {
            return recruiter.getPostedMissions();
        }
        return List.of();
    }
}
