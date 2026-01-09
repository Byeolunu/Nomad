package nutar.back.web;
import nutar.back.dao.entites.Application;
import nutar.back.dao.entites.Contract;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Mission;
import nutar.back.dao.enums.ApplicationStatus;
import nutar.back.dao.enums.ContractStatus;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.dao.repositories.MissionRepository;
import nutar.back.dao.repositories.ContractRepository;
import nutar.back.service.ApplicationService;
import nutar.back.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private FreelancerRepository freelancerRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private nutar.back.dao.repositories.ApplicationRepository applicationRepository;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractRepository contractRepository;

    @PostMapping("/apply")
    public ResponseEntity<Application> applyToMission(@RequestBody Application application) {
        Application savedApplication = applicationService.applyToMission(application);
        return ResponseEntity.ok(savedApplication);
    }

    @PostMapping("/mission/{missionId}")
    public ResponseEntity<Map<String, Object>> applyToMissionById(
            @PathVariable Long missionId,
            @RequestBody Map<String, Object> applicationData,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = authentication.getName();

            Freelancer freelancer = freelancerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Freelancer not found"));

            Mission mission = missionRepository.findById(missionId)
                    .orElseThrow(() -> new RuntimeException("Mission not found"));

            Application application = new Application();
            application.setFreelancer(freelancer);
            application.setMission(mission);
            application.setCoverLetter((String) applicationData.get("coverLetter"));
            application.setProposedBudget(applicationData.get("proposedBudget") != null
                    ? Double.valueOf(applicationData.get("proposedBudget").toString()) : null);
            application.setEstimatedDays(applicationData.get("estimatedDays") != null
                    ? Integer.valueOf(applicationData.get("estimatedDays").toString()) : null);
            application.setAdditionalNotes((String) applicationData.get("additionalNotes"));
            application.setStatus(ApplicationStatus.PENDING);
            application.setAppliedDate(LocalDateTime.now());
            application.setUpdatedDate(LocalDateTime.now());

            Application savedApplication = applicationService.applyToMission(application);

            response.put("success", true);
            response.put("message", "Application submitted successfully");
            response.put("applicationId", savedApplication.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to submit application: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/mission/{missionId}")
    public ResponseEntity<List<Application>> getApplicationsForMission(@PathVariable Long missionId) {
        List<Application> applications = applicationService.getApplicationsForMission(missionId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<Application>> getFreelancerApplications(@PathVariable Long freelancerId) {
        List<Application> applications = applicationService.getFreelancerApplications(freelancerId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/test/{freelancerId}")
    public ResponseEntity<Map<String, Object>> testFreelancerApplications(@PathVariable Long freelancerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Application> applications = applicationService.getFreelancerApplications(freelancerId);
            result.put("success", true);
            result.put("freelancerId", freelancerId);
            result.put("applicationCount", applications.size());
            result.put("applications", applications);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/mission/{missionId}/proposals")
    public ResponseEntity<Map<String, Object>> getProposalsForMission(@PathVariable Long missionId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Application> applications = applicationService.getApplicationsForMission(missionId);
            result.put("success", true);
            result.put("missionId", missionId);
            result.put("proposalCount", applications.size());
            result.put("proposals", applications.stream().map(app -> {
                Map<String, Object> proposal = new HashMap<>();
                proposal.put("id", app.getId());
                proposal.put("freelancerId", app.getFreelancer() != null ? app.getFreelancer().getId() : null);
                proposal.put("freelancerName", app.getFreelancer() != null ? 
                        app.getFreelancer().getFirstName() + " " + app.getFreelancer().getLastName() : "Unknown");
                proposal.put("freelancerEmail", app.getFreelancer() != null ? app.getFreelancer().getEmail() : null);
                proposal.put("coverLetter", app.getCoverLetter());
                proposal.put("proposedBudget", app.getProposedBudget());
                proposal.put("estimatedDays", app.getEstimatedDays());
                proposal.put("status", app.getStatus());
                proposal.put("appliedDate", app.getAppliedDate());
                return proposal;
            }).toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/{applicationId}/accept")
    public ResponseEntity<Map<String, Object>> acceptProposal(@PathVariable Long applicationId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));
            
            Application updated = applicationService.updateApplicationStatus(applicationId, "ACCEPTED");
            
            Contract contract = new Contract();
            contract.setFreelancer(application.getFreelancer());
            contract.setMission(application.getMission());
            contract.setRecruiter(application.getMission().getRecruiter());
            contract.setTitle("Contract: " + application.getMission().getTitle());
            contract.setDescription("Contract for mission: " + application.getMission().getDescription());
            contract.setBudget(application.getMission().getBudget());
            contract.setStatus(ContractStatus.IN_PROGRESS);
            contract.setStartDate(LocalDateTime.now());
            
            Contract savedContract = contractService.createContract(contract);
            
            result.put("success", true);
            result.put("message", "Proposal accepted successfully and contract registered");
            result.put("applicationId", updated.getId());
            result.put("status", updated.getStatus());
            result.put("contractId", savedContract.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/{applicationId}/reject")
    public ResponseEntity<Map<String, Object>> rejectProposal(@PathVariable Long applicationId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Application updated = applicationService.updateApplicationStatus(applicationId, "REJECTED");
            result.put("success", true);
            result.put("message", "Proposal rejected successfully");
            result.put("applicationId", updated.getId());
            result.put("status", updated.getStatus());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/freelancer/{freelancerId}/active-missions")
    public ResponseEntity<Map<String, Object>> getFreelancerActiveMissions(@PathVariable Long freelancerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Application> allApplications = applicationRepository.findByFreelancerId(freelancerId);
            
            List<Application> acceptedApplications = allApplications.stream()
                    .filter(app -> {
                        System.out.println("DEBUG: Application ID: " + app.getId() + ", Status: " + app.getStatus());
                        return app.getStatus() == ApplicationStatus.ACCEPTED;
                    })
                    .toList();
            
            List<Map<String, Object>> missions = acceptedApplications.stream()
                    .filter(app -> {
                        if (app.getMission() == null) {
                            return false;
                        }
                        return true;
                    })
                    .map(app -> {
                        try {
                            Map<String, Object> missionMap = new HashMap<>();
                            Mission mission = app.getMission();
                            missionMap.put("id", mission.getId());
                            missionMap.put("applicationId", app.getId()); // Include application ID
                            missionMap.put("title", mission.getTitle());
                            missionMap.put("description", mission.getDescription());
                            missionMap.put("budget", mission.getBudget());
                            missionMap.put("type", mission.getType());
                            missionMap.put("deadline", mission.getDeadline());
                            missionMap.put("status", "In progress");
                            
                            String recruiterName = "Unknown";
                            if (mission.getRecruiter() != null) {
                                recruiterName = (mission.getRecruiter().getFirstName() != null ? mission.getRecruiter().getFirstName() : "") + 
                                               " " + (mission.getRecruiter().getLastName() != null ? mission.getRecruiter().getLastName() : "");
                            }
                            missionMap.put("recruiterName", recruiterName.trim());
                            missionMap.put("appliedDate", app.getAppliedDate());
                            missionMap.put("acceptedDate", app.getUpdatedDate());
                            
                            return missionMap;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(m -> m != null)
                    .toList();
            
            result.put("success", true);
            result.put("activeMissionsCount", missions.size());
            result.put("activeMissions", missions);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("activeMissionsCount", 0);
            result.put("activeMissions", new ArrayList<>());
            return ResponseEntity.ok(result); 
        }
    }

    @GetMapping("/debug/all")
    public ResponseEntity<Map<String, Object>> debugAllApplications() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Application> allApps = applicationRepository.findAll();
            result.put("success", true);
            result.put("totalApplications", allApps.size());
            result.put("applications", allApps.stream().map(app -> {
                Map<String, Object> appMap = new HashMap<>();
                appMap.put("id", app.getId());
                appMap.put("freelancerId", app.getFreelancer() != null ? app.getFreelancer().getId() : null);
                appMap.put("freelancerEmail", app.getFreelancer() != null ? app.getFreelancer().getEmail() : null);
                appMap.put("missionId", app.getMission() != null ? app.getMission().getId() : null);
                appMap.put("missionTitle", app.getMission() != null ? app.getMission().getTitle() : null);
                appMap.put("status", app.getStatus());
                appMap.put("appliedDate", app.getAppliedDate());
                return appMap;
            }).toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}
