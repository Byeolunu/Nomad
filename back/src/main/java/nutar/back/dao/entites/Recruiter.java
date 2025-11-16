package nutar.back.dao.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Entity
@Table(name = "recruiters")
public class Recruiter extends User
{
    private String companyName;
    private String companyWebsite;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Mission> postedMissions = new ArrayList<>();

}