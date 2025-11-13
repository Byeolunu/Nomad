package nutar.back.service;

import nutar.back.dao.entites.Mission;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.enums.MissionStatus;
import nutar.back.dao.repositories.MissionRepository;
import nutar.back.dao.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionManager implements MissionService {
    @Autowired
    MissionRepository missionRepository;

    @Autowired
    RecruiterRepository recruiterRepository;

    @Override
    public List<Mission> getAllOpenMissions() {
        return missionRepository.findByStatus(MissionStatus.OPEN);
    }

    @Override
    public Mission getMissionById(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + id));
    }

    @Override
    public Mission createMission(Mission mission, Long recruiterId)
    {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with id: " + recruiterId));

        mission.setRecruiter(recruiter);
        mission.setStatus(MissionStatus.OPEN);
        mission.setCreatedAt(java.time.LocalDateTime.now());
        return missionRepository.save(mission);
    }
    @Override
    public List<Mission> getMissionsByRecruiter(Long recruiterId) {
        return missionRepository.findByRecruiterId(recruiterId);
    }
}


