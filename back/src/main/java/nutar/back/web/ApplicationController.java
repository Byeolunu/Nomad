package nutar.back.web;
import nutar.back.dao.entites.Application;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Mission;
import nutar.back.dao.enums.ApplicationStatus;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.dao.repositories.MissionRepository;
import nutar.back.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
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
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(@PathVariable Long applicationId, @RequestBody String status) {
        Application updatedApplication = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(updatedApplication);
    }
}
