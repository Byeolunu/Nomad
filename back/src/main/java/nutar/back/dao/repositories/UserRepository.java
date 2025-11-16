package nutar.back.dao.repositories;

import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT f FROM Freelancer f")
    List<Freelancer> findAllFreelancers();

    @Query("SELECT r FROM Recruiter r")
    List<Recruiter> findAllRecruiters();
}