package nutar.back.dao.entites;

import jakarta.persistence.*;
import nutar.back.dao.enums.SkillCategory;
import java.util.HashSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    @ManyToMany(mappedBy = "skills")
    private Set<Freelancer> freelancers = new HashSet<>();

    @ManyToMany(mappedBy = "requiredSkills")
    private Set<Mission> missions = new HashSet<>();

}