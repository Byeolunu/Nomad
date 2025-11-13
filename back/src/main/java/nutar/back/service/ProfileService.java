package nutar.back.service;

import nutar.back.dao.entites.Profile;
import nutar.back.dao.enums.ExperienceLevel;
import java.util.List;
import java.util.Optional;

public interface ProfileService {
    public Profile updateProfile(Long profileId, Profile profile);
    public Profile getProfileByFreelancer(Long freelancerId);
}
