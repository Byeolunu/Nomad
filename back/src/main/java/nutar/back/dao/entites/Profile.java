package nutar.back.dao.entites;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nutar.back.dao.enums.ExperienceLevel;
import java.util.ArrayList;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profiles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 1000)
    private String bio;
    @OneToOne(mappedBy = "profile")
    @JsonIgnore
    private Freelancer freelancer;
    @ManyToMany
    @JsonIgnoreProperties({"missions", "hibernateLazyInitializer", "handler"})
    private List<Skill> skills;
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    private String location;
    private String profilePicture;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private List<WorkExperience> workExperiences = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private List<Education> educations = new ArrayList<>();
}
