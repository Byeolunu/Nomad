package nutar.back.web;

import nutar.back.dao.entites.Application;
import nutar.back.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<Application> applyToMission(@RequestBody Application application) {
        Application savedApplication = applicationService.applyToMission(application);
        return ResponseEntity.ok(savedApplication);
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