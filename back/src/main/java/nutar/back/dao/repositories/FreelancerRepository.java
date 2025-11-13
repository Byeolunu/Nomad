package nutar.back.dao.repositories;

import nutar.back.dao.entites.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {

    Optional<Freelancer> findByEmail(String email);
    List<Freelancer> findByProfileSkillsContainingIgnoreCase(String skill);
    List<Freelancer> findByTitleContainingIgnoreCase(String keyword);

    List<Freelancer> findByHourlyRateBetween(Double minRate, Double maxRate);
}