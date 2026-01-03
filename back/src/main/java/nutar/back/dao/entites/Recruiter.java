package nutar.back.dao.entites;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recruiters")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "postedMissions"})
@ToString(exclude = "postedMissions")
@EqualsAndHashCode(exclude = "postedMissions", callSuper = true)
public class Recruiter extends User
{
    private String companyName;
    private String companyWebsite;
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Mission> postedMissions = new ArrayList<>();
}
