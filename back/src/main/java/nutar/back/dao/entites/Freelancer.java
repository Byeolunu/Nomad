package nutar.back.dao.entites;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "freelancers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Freelancer extends User {
    private String title;
    @Column(length = 1000)
    private String summary;
    private Double hourlyRate;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @JsonIgnoreProperties({"freelancer", "hibernateLazyInitializer", "handler"})
    private Profile profile;
    @OneToMany(mappedBy = "freelancer")
    @JsonIgnoreProperties({"freelancer", "mission"})
    private List<Application> applications = new ArrayList<>();
    @ManyToMany
    @JsonIgnoreProperties({"missions", "hibernateLazyInitializer", "handler"})
    private List<Skill> skills;
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"freelancer"})
    private List<Review> reviews = new ArrayList<>();
}
