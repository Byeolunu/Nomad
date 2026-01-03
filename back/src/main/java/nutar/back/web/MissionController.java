package nutar.back.web;
import nutar.back.dao.entites.Mission;
import nutar.back.dao.entites.Recruiter;
import nutar.back.service.MissionService;
import nutar.back.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
@RestController
@RequestMapping("/api/missions")
@CrossOrigin(origins = "http://localhost:4200")
public class MissionController {
    @Autowired
    private MissionService missionService;
    @Autowired
    private RecruiterService recruiterService;
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Mission>> getAllMissions() {
        List<Mission> missions = missionService.getAllOpenMissions();
        return ResponseEntity.ok(missions);
    }
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Mission> getMissionById(@PathVariable Long id) {
        Mission mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }
    @PostMapping
    @Transactional
    public ResponseEntity<?> createMission(@RequestBody Mission mission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Recruiter recruiter = recruiterService.getRecruiterByEmail(email);
        if (recruiter == null) {
            return ResponseEntity.badRequest().body("Recruiter not found");
        }
        Mission createdMission = missionService.createMission(mission, recruiter.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdMission.getId());
        response.put("title", createdMission.getTitle());
        response.put("message", "Mission created successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Mission>> getMissionsByRecruiter(@PathVariable Long recruiterId) {
        List<Mission> missions = missionService.getMissionsByRecruiter(recruiterId);
        return ResponseEntity.ok(missions);
    }
    @GetMapping("/recruiter")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Mission>> getMyMissions(Authentication authentication) {
        String email = authentication.getName();
        Recruiter recruiter = recruiterService.getRecruiterByEmail(email);
        if (recruiter == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Mission> missions = missionService.getMissionsByRecruiter(recruiter.getId());
        return ResponseEntity.ok(missions);
    }
}
