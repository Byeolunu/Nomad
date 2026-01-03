package nutar.back.service;
import nutar.back.dao.entites.Mission;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.enums.MissionStatus;
import nutar.back.dao.repositories.ApplicationRepository;
import nutar.back.dao.repositories.MissionRepository;
import nutar.back.dao.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@Transactional
public class MissionManager implements MissionService {
    @Autowired
    MissionRepository missionRepository;
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    ApplicationRepository applicationRepository;
    @Override
    public List<Mission> getAllOpenMissions() {
        List<Mission> missions = missionRepository.findByStatus(MissionStatus.OPEN);
        missions.forEach(mission -> {
            Integer count = applicationRepository.countByMissionId(mission.getId());
            mission.setApplicationCount(count != null ? count : 0);
        });
        return missions;
    }
    @Override
    public Mission getMissionById(Long id) 
    {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + id));
        Integer count = applicationRepository.countByMissionId(mission.getId());
        mission.setApplicationCount(count != null ? count : 0);
        return mission;
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
        List<Mission> missions = missionRepository.findByRecruiterId(recruiterId);
        missions.forEach(mission -> {
            Integer count = applicationRepository.countByMissionId(mission.getId());
            mission.setApplicationCount(count != null ? count : 0);
        });
        return missions;
    }
}
