package nutar.back.dao.repositories;
import nutar.back.dao.entites.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByFreelancerId(Long freelancerId);
}
