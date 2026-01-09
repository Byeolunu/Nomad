package nutar.back.web;
import nutar.back.dao.entites.User;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Profile;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.Skill;
import nutar.back.dao.repositories.UserRepository;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.dao.repositories.SkillRepository;
import nutar.back.service.UserService;
import nutar.back.service.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
    @Autowired
    private SkillRepository skillRepository;
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
            
            if (user instanceof Freelancer) {
                Freelancer freelancer = (Freelancer) user;
                
                if (updates.containsKey("name")) {
                    String fullName = (String) updates.get("name");
                    String[] nameParts = fullName.trim().split("\\s+", 2);
                    freelancer.setFirstName(nameParts[0]);
                    if (nameParts.length > 1) {
                        freelancer.setLastName(nameParts[1]);
                    } else {
                        freelancer.setLastName("");
                    }
                }
                
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

                if (updates.containsKey("skillIds")) {
                    Object rawSkillIds = updates.get("skillIds");
                    if (rawSkillIds instanceof List<?>) {
                        List<Long> skillIds = ((List<?>) rawSkillIds).stream()
                                .map(val -> {
                                    if (val instanceof Number) {
                                        return ((Number) val).longValue();
                                    }
                                    try {
                                        return Long.valueOf(val.toString());
                                    } catch (NumberFormatException ex) {
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());

                        if (!skillIds.isEmpty()) {
                            List<Skill> skills = skillRepository.findAllById(skillIds);
                            freelancer.setSkills(skills);
                        } else {
                            freelancer.setSkills(new ArrayList<>());
                        }
                    }
                }
                
                // Handle profile updates
                if (updates.containsKey("bio") || updates.containsKey("location") || 
                    updates.containsKey("profilePicture") || updates.containsKey("experienceLevel")) {
                    Profile profile = freelancer.getProfile();
                    if (profile == null) {
                        profile = new Profile();
                        profile.setFreelancer(freelancer);
                        profile.setBio("");
                        freelancer.setProfile(profile);
                    }
                    if (updates.containsKey("bio")) {
                        String bio = (String) updates.get("bio");
                        profile.setBio(bio != null ? bio : "");
                    }
                    if (updates.containsKey("location")) {
                        String location = (String) updates.get("location");
                        profile.setLocation(location != null ? location : "");
                    }
                    if (updates.containsKey("profilePicture")) {
                        String profilePic = (String) updates.get("profilePicture");
                        profile.setProfilePicture(profilePic != null ? profilePic : "");
                    }
                    if (updates.containsKey("experienceLevel")) {
                        String expLevel = (String) updates.get("experienceLevel");
                        if (expLevel != null && !expLevel.trim().isEmpty()) {
                            try {
                                profile.setExperienceLevel(nutar.back.dao.enums.ExperienceLevel.valueOf(expLevel));
                            } catch (IllegalArgumentException e) {
                                System.err.println("Invalid experience level: " + expLevel);
                            }
                        }
                    }
                }
                
                Freelancer updated = freelancerService.updateFreelancer(freelancer);
                System.out.println("DEBUG: Freelancer updated: " + updated.getId());
                System.out.println("DEBUG: Profile: " + (updated.getProfile() != null ? updated.getProfile().getId() : "null"));
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
