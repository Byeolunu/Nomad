package nutar.back.service;
import nutar.back.dao.entites.Profile;
import nutar.back.dao.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ProfileManager implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Override
    public Profile getProfileByFreelancer(Long freelancerId) {
        return profileRepository.findByFreelancerId(freelancerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }
    @Override
    public Profile updateProfile(Long profileId, Profile profile) {
        Profile existingProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        if (profile.getSkills() != null) {
            existingProfile.setSkills(profile.getSkills());
        }
        if (profile.getExperienceLevel() != null) {
            existingProfile.setExperienceLevel(profile.getExperienceLevel());
        }
        if (profile.getBio() != null) {
            existingProfile.setBio(profile.getBio());
        }
        if (profile.getLocation() != null) {
            existingProfile.setLocation(profile.getLocation());
        }
        if (profile.getProfilePicture() != null) {
            existingProfile.setProfilePicture(profile.getProfilePicture());
        }
        return profileRepository.save(existingProfile);
    }
}
