package nutar.back.service;
import nutar.back.dao.entites.Contract;
import nutar.back.dao.enums.ContractStatus;
import nutar.back.dao.repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class ContractManager implements ContractService {
    @Autowired
    private ContractRepository contractRepository;
    @Override
    public Contract createContract(Contract contract) {
        contract.setStatus(ContractStatus.IN_PROGRESS);
        return contractRepository.save(contract);
    }
    @Override
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }
    @Override
    public List<Contract> getContractsByRecruiter(Long recruiterId) {
        return contractRepository.findByRecruiterId(recruiterId);
    }
    @Override
    public List<Contract> getContractsByFreelancer(Long freelancerId) {
        return contractRepository.findByFreelancerId(freelancerId);
    }
    @Override
    public Contract updateContractStatus(Long contractId, ContractStatus status) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        contract.setStatus(status);
        return contractRepository.save(contract);
    }
}
