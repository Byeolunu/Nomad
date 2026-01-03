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
        existingProfile.setSkills(profile.getSkills());
        existingProfile.setExperienceLevel(profile.getExperienceLevel());
        existingProfile.setBio(profile.getBio());
        existingProfile.setLocation(profile.getLocation());
        return profileRepository.save(existingProfile);
    }
}
