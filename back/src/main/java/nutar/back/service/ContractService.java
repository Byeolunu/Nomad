package nutar.back.service;
import nutar.back.dao.entites.Contract;
import nutar.back.dao.enums.ContractStatus;
import java.util.List;
import java.util.Optional;
public interface ContractService {
    Contract createContract(Contract contract);
    Optional<Contract> getContractById(Long id);
    List<Contract> getContractsByRecruiter(Long recruiterId);
    List<Contract> getContractsByFreelancer(Long freelancerId);
    Contract updateContractStatus(Long contractId, ContractStatus status);
}
