package nutar.back.dao.repositories;

import nutar.back.dao.entites.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// repositories/WorkExperienceRepository.java
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findByProfileId(Long profileId);
    List<WorkExperience> findByProfileFreelancerId(Long freelancerId);
    void deleteByProfileId(Long profileId);
    List<WorkExperience> findByCompanyContainingIgnoreCase(String companyName);
}