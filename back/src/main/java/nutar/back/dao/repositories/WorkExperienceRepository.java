package nutar.back.dao.repositories;

import nutar.back.dao.entites.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findByProfileId(Long profileId);
    List<WorkExperience> findByProfileFreelancerId(Long freelancerId);
    void deleteByProfileId(Long profileId);
    List<WorkExperience> findByCompanyContainingIgnoreCase(String companyName);
}