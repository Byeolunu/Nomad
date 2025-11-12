package nutar.back.dao.repositories;

import nutar.back.dao.entites.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByMissionId(Long missionId);
    List<Application> findByFreelancerId(Long freelancerId);
    Optional<Application> findByFreelancerIdAndMissionId(Long freelancerId, Long missionId);
    Boolean existsByFreelancerIdAndMissionId(Long freelancerId, Long missionId);
    List<Application> findByMissionRecruiterId(Long recruiterId);
}
