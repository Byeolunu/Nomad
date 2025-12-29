package nutar.back.service;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.User;
import java.util.List;
import java.util.Optional;
public interface UserService {
    public List<Freelancer> getAllFreelancers();
    public List<Recruiter> getAllRecruiters();
    public List<User> getAllUsers();
    public User getUserById(Long id);
}
