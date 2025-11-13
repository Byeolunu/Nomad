package nutar.back.dao.repositories;

import nutar.back.dao.entites.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import nutar.back.dao.enums.SkillCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByName(String name);
    List<Skill> findByCategory(SkillCategory category);
    List<Skill> findByNameContainingIgnoreCase(String name);

    @Query("SELECT s FROM Skill s JOIN s.freelancers f WHERE f.id = :freelancerId")
    List<Skill> findByFreelancerId(@Param("freelancerId") Long freelancerId);
} 