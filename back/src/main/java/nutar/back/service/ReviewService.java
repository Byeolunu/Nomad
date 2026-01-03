package nutar.back.service;

import nutar.back.dao.entites.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review createReview(Long freelancerId, Long recruiterId, Integer rating, String comment);
    Review updateReview(Long reviewId, Long recruiterId, Integer rating, String comment);
    Optional<Review> getReviewById(Long id);
    List<Review> getReviewsByFreelancerId(Long freelancerId);
    List<Review> getReviewsByRecruiterId(Long recruiterId);
    Optional<Review> getRecruiterReviewForFreelancer(Long recruiterId, Long freelancerId);
    boolean canRecruiterReviewFreelancer(Long recruiterId, Long freelancerId);
    boolean hasRecruiterReviewedFreelancer(Long recruiterId, Long freelancerId);
    void deleteReview(Long id);
}
