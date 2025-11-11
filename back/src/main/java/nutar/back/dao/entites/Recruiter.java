package nutar.back.dao.entites;

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
    private List<Mission> postedMissions = new ArrayList<>();

}