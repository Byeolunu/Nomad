package nutar.back.service;

import nutar.back.dao.entites.Skill;
import nutar.back.dao.enums.SkillCategory;
import nutar.back.dao.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SkillManager implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public Skill getSkillById(Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Skill updateSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    @Override
    public Optional<Skill> getByName(String name) {
        return skillRepository.findByName(name);
    }

    @Override
    public List<Skill> findByCategory(SkillCategory category) {
        return skillRepository.findByCategory(category);
    }

    @Override
    public List<Skill> findByFreelancerId(Long freelancerId) {
        return skillRepository.findByFreelancerId(freelancerId);
    }
}
