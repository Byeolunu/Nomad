package nutar.back.service;

import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.repositories.FreelancerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FreelancerManager implements FreelancerService {

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Override
    public Freelancer createFreelancer(Freelancer freelancer) {
        return freelancerRepository.save(freelancer);
    }

    @Override
    public Optional<Freelancer> getFreelancerById(Long id) {
        return freelancerRepository.findById(id);
    }

    @Override
    public List<Freelancer> getAllFreelancers() {
        return freelancerRepository.findAll();
    }

    @Override
    public Freelancer updateFreelancer(Freelancer freelancer) {
        return freelancerRepository.save(freelancer);
    }

    @Override
    public void deleteFreelancer(Long id) {
        freelancerRepository.deleteById(id);
    }

    @Override
    public List<Freelancer> findByTitle(String title) {
        return freelancerRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Freelancer> findByHourlyRateBetween(Double minRate, Double maxRate) {
        return freelancerRepository.findByHourlyRateBetween(minRate, maxRate);
    }
}
