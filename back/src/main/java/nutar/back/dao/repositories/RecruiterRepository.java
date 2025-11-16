package nutar.back.dao.repositories;

import nutar.back.dao.entites.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    List<Recruiter> findByCompanyNameContainingIgnoreCase(String companyName);

}