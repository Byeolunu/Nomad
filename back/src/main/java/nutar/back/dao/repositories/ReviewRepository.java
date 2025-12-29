package nutar.back.dao.repositories;
import nutar.back.dao.entites.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFreelancerId(Long freelancerId);
    List<Review> findByRecruiterId(Long recruiterId);
    List<Review> findByMissionId(Long missionId);
    List<Review> findByFreelancerIdOrderByCreatedAtDesc(Long freelancerId);
}
