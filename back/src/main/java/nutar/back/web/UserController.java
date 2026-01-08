package nutar.back.web;
import nutar.back.dao.entites.User;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.repositories.UserRepository;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.service.UserService;
import nutar.back.service.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FreelancerRepository freelancerRepository;
    @Autowired
    private FreelancerService freelancerService;
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Check if it's a Freelancer
            if (user instanceof Freelancer) {
                Freelancer freelancer = (Freelancer) user;
                
                // Update fields if provided
                if (updates.containsKey("phoneNumber")) {
                    freelancer.setPhoneNumber((String) updates.get("phoneNumber"));
                }
                if (updates.containsKey("title")) {
                    freelancer.setTitle((String) updates.get("title"));
                }
                if (updates.containsKey("hourlyRate")) {
                    freelancer.setHourlyRate(Double.valueOf(updates.get("hourlyRate").toString()));
                }
                if (updates.containsKey("summary")) {
                    freelancer.setSummary((String) updates.get("summary"));
                }
                
                Freelancer updated = freelancerService.updateFreelancer(freelancer);
                System.out.println("DEBUG: Freelancer updated: " + updated.getId());
                return ResponseEntity.ok(updated);
            }
            
            return ResponseEntity.badRequest().body(Map.of("error", "User is not a freelancer"));
        } catch (Exception e) {
            System.out.println("DEBUG ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }
    @GetMapping("/freelancers")
    public ResponseEntity<List<Freelancer>> getAllFreelancers() {
        List<Freelancer> freelancers = userRepository.findAllFreelancers();
        return ResponseEntity.ok(freelancers);
    }
    @GetMapping("/recruiters")
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        List<Recruiter> recruiters = userRepository.findAllRecruiters();
        return ResponseEntity.ok(recruiters);
    }
}
