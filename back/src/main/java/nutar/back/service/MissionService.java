package nutar.back.service;
import nutar.back.dao.entites.Mission;
import java.util.List;
public interface MissionService {
    public List<Mission> getAllOpenMissions();
    public Mission getMissionById(Long id);
    public Mission createMission(Mission mission, Long recruiterId);
    public List<Mission> getMissionsByRecruiter(Long recruiterId);
}
