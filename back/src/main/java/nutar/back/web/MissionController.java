package nutar.back.web;

import nutar.back.dao.entites.Mission;
import nutar.back.dao.entites.Recruiter;
import nutar.back.service.MissionService;
import nutar.back.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@CrossOrigin(origins = "http://localhost:4200")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @Autowired
    private RecruiterService recruiterService;

    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions() {
        List<Mission> missions = missionService.getAllOpenMissions();
        return ResponseEntity.ok(missions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mission> getMissionById(@PathVariable Long id) {
        Mission mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }

    @PostMapping
    public ResponseEntity<Mission> createMission(@RequestBody Mission mission , Recruiter recruiter) {
        Mission createdMission = missionService.createMission(mission,recruiter.getId());
        return ResponseEntity.ok(createdMission);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Mission>> getMissionsByRecruiter(@PathVariable Long recruiterId) {
        List<Mission> missions = missionService.getMissionsByRecruiter(recruiterId);
        return ResponseEntity.ok(missions);
    }
}