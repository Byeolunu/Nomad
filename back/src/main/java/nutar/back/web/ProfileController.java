package nutar.back.web;

import nutar.back.dao.entites.Profile;
import nutar.back.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<Profile> getProfileByFreelancer(@PathVariable Long freelancerId) {
        Profile profile = profileService.getProfileByFreelancer(freelancerId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long profileId, @RequestBody Profile profile) {
        Profile updatedProfile = profileService.updateProfile(profileId, profile);
        return ResponseEntity.ok(updatedProfile);
    }
}