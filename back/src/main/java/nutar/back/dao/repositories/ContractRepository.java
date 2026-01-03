package nutar.back.dao.repositories;
import nutar.back.dao.entites.Contract;
import nutar.back.dao.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByRecruiterId(Long recruiterId);
    List<Contract> findByFreelancerId(Long freelancerId);
    List<Contract> findByStatus(ContractStatus status);
    List<Contract> findByRecruiterIdAndFreelancerId(Long recruiterId, Long freelancerId);
}
