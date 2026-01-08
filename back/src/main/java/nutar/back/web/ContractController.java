package nutar.back.web;
import nutar.back.dao.entites.Contract;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Mission;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.enums.ContractStatus;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.dao.repositories.MissionRepository;
import nutar.back.dao.repositories.RecruiterRepository;
import nutar.back.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "http://localhost:4200")
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private RecruiterRepository recruiterRepository;
    @Autowired
    private FreelancerRepository freelancerRepository;
    @Autowired
    private MissionRepository missionRepository;
    @PostMapping("/hire/{freelancerId}")
    public ResponseEntity<Map<String, Object>> hireFreelancer(
            @PathVariable Long freelancerId,
            @RequestBody Map<String, Object> contractData,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = authentication.getName();
            Recruiter recruiter = recruiterRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));
            Freelancer freelancer = freelancerRepository.findById(freelancerId)
                    .orElseThrow(() -> new RuntimeException("Freelancer not found"));
            Contract contract = new Contract();
            contract.setRecruiter(recruiter);
            contract.setFreelancer(freelancer);
            contract.setTitle((String) contractData.get("title"));
            contract.setDescription((String) contractData.get("description"));
            contract.setBudget(contractData.get("budget") != null 
                    ? Double.valueOf(contractData.get("budget").toString()) : null);
            contract.setStatus(ContractStatus.IN_PROGRESS);
            if (contractData.get("missionId") != null) {
                Long missionId = Long.valueOf(contractData.get("missionId").toString());
                Mission mission = missionRepository.findById(missionId).orElse(null);
                contract.setMission(mission);
            }
            if (contractData.get("startDate") != null) {
                contract.setStartDate(LocalDateTime.parse((String) contractData.get("startDate")));
            }
            if (contractData.get("endDate") != null) {
                contract.setEndDate(LocalDateTime.parse((String) contractData.get("endDate")));
            }
            Contract savedContract = contractService.createContract(contract);
            response.put("success", true);
            response.put("message", "Hiring request sent successfully!");
            response.put("contractId", savedContract.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to send hiring request: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/recruiter")
    public ResponseEntity<List<Contract>> getRecruiterContracts(Authentication authentication) {
        String email = authentication.getName();
        Recruiter recruiter = recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return ResponseEntity.ok(contractService.getContractsByRecruiter(recruiter.getId()));
    }
    @GetMapping("/freelancer")
    public ResponseEntity<List<Contract>> getFreelancerContracts(Authentication authentication) {
        String email = authentication.getName();
        Freelancer freelancer = freelancerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));
        return ResponseEntity.ok(contractService.getContractsByFreelancer(freelancer.getId()));
    }
    @PutMapping("/{contractId}/status")
    public ResponseEntity<Map<String, Object>> updateContractStatus(
            @PathVariable Long contractId,
            @RequestBody Map<String, String> statusData) {
        Map<String, Object> response = new HashMap<>();
        try {
            ContractStatus status = ContractStatus.valueOf(statusData.get("status").toUpperCase());
            Contract updated = contractService.updateContractStatus(contractId, status);
            response.put("success", true);
            response.put("message", "Contract status updated");
            response.put("contractId", updated.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update contract: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
