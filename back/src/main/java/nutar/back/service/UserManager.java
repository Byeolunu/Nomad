package nutar.back.service;

import nutar.back.dao.entites.User;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManager implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    public List<Freelancer> getAllFreelancers() {
        return userRepository.findAllFreelancers();
    }

    @Override
    public List<Recruiter> getAllRecruiters() {
        return userRepository.findAllRecruiters();
    }
}