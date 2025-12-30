package nutar.back.service;

import nutar.back.dao.entites.Application;
import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.Review;
import nutar.back.dao.repositories.ApplicationRepository;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.dao.repositories.RecruiterRepository;
import nutar.back.dao.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewManager implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public Review createReview(Long freelancerId, Long recruiterId, Integer rating, String comment) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (hasRecruiterReviewedFreelancer(recruiterId, freelancerId)) {
            throw new RuntimeException("You have already reviewed this freelancer");
        }

        if (!canRecruiterReviewFreelancer(recruiterId, freelancerId)) {
            throw new RuntimeException("You can only review freelancers you have worked with");
        }

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setAuthorName(recruiter.getFirstName() + " " + recruiter.getLastName());
        review.setFreelancer(freelancer);
        review.setRecruiter(recruiter);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> getReviewsByFreelancerId(Long freelancerId) {
        return reviewRepository.findByFreelancerIdOrderByCreatedAtDesc(freelancerId);
    }

    @Override
    public List<Review> getReviewsByRecruiterId(Long recruiterId) {
        return reviewRepository.findByRecruiterId(recruiterId);
    }

    @Override
    public boolean canRecruiterReviewFreelancer(Long recruiterId, Long freelancerId) {
        List<Application> applications = applicationRepository.findByFreelancerId(freelancerId);
        
        for (Application app : applications) {
            if (app.getMission() != null && 
                app.getMission().getRecruiter() != null &&
                app.getMission().getRecruiter().getId().equals(recruiterId) &&
                (app.getStatus().toString().equals("ACCEPTED") || 
                 app.getStatus().toString().equals("COMPLETED"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasRecruiterReviewedFreelancer(Long recruiterId, Long freelancerId) {
        List<Review> reviews = reviewRepository.findByFreelancerId(freelancerId);
        return reviews.stream()
                .anyMatch(r -> r.getRecruiter() != null && r.getRecruiter().getId().equals(recruiterId));
    }

    @Override
    public Optional<Review> getRecruiterReviewForFreelancer(Long recruiterId, Long freelancerId) {
        List<Review> reviews = reviewRepository.findByFreelancerId(freelancerId);
        return reviews.stream()
                .filter(r -> r.getRecruiter() != null && r.getRecruiter().getId().equals(recruiterId))
                .findFirst();
    }

    @Override
    @Transactional
    public Review updateReview(Long reviewId, Long recruiterId, Integer rating, String comment) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Verify ownership
        if (review.getRecruiter() == null || !review.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You can only edit your own reviews");
        }

        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
