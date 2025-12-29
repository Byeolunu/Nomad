package nutar.back.service;
import nutar.back.dao.entites.Skill;
import nutar.back.dao.enums.SkillCategory;
import java.util.List;
import java.util.Optional;
public interface SkillService {
    Skill createSkill(Skill skill);
    Skill getSkillById(Long id);
    List<Skill> getAllSkills();
    Skill updateSkill(Skill skill);
    void deleteSkill(Long id);
    Optional<Skill> getByName(String name);
    List<Skill> findByCategory(SkillCategory category);
    List<Skill> findByFreelancerId(Long freelancerId);
}
