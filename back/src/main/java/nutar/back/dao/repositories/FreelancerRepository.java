package nutar.back.dao.repositories;

import nutar.back.dao.entites.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    Optional<Freelancer> findByEmail(String email);
    List<Freelancer> findByProfileSkillsContainingIgnoreCase(String skill);
    List<Freelancer> findByTitleContainingIgnoreCase(String keyword);
}