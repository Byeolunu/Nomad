package nutar.back.service;
import nutar.back.dao.entites.Freelancer;
import java.util.List;
import java.util.Optional;
public interface FreelancerService {
    Freelancer createFreelancer(Freelancer freelancer);
    Optional<Freelancer> getFreelancerById(Long id);
    List<Freelancer> getAllFreelancers();
    Freelancer updateFreelancer(Freelancer freelancer);
    void deleteFreelancer(Long id);
    List<Freelancer> findByTitle(String title);
    List<Freelancer> findByHourlyRateBetween(Double minRate, Double maxRate);
}
