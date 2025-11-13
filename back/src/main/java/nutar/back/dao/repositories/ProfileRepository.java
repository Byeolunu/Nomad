package nutar.back.dao.repositories;

import nutar.back.dao.entites.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import nutar.back.dao.enums.ExperienceLevel;
import org.springframework.stereotype.Repository;

@Repository

public interface ProfileRepository extends JpaRepository<Profile, Long>
{
    Optional<Profile> findByFreelancerId(Long freelancerId);
    List<Profile> findBySkillsContainingIgnoreCase(String skill);
    List<Profile> findByExperienceLevel(ExperienceLevel experienceLevel);
    List<Profile> findByLocationContainingIgnoreCase(String location);
}