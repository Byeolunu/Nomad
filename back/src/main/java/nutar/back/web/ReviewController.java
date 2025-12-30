package nutar.back.web;

import nutar.back.dao.entites.Review;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dto.review.CanReviewDTO;
import nutar.back.dto.review.CreateReviewDTO;
import nutar.back.dto.review.ReviewDTO;
import nutar.back.mappers.ReviewMapper;
import nutar.back.service.RecruiterService;
import nutar.back.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private ReviewMapper reviewMapper;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createReview(@RequestBody CreateReviewDTO request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            Recruiter recruiter = recruiterService.getRecruiterByEmail(email);
            if (recruiter == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only recruiters can leave reviews"));
            }

            if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Rating must be between 1 and 5"));
            }

            if (request.getFreelancerId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Freelancer ID is required"));
            }

            Review review = reviewService.createReview(
                request.getFreelancerId(),
                recruiter.getId(),
                request.getRating(),
                request.getComment()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("id", review.getId());
            response.put("message", "Review created successfully");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/freelancer/{freelancerId}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<ReviewDTO>> getFreelancerReviews(@PathVariable Long freelancerId) {
        List<Review> reviews = reviewService.getReviewsByFreelancerId(freelancerId);
        List<ReviewDTO> response = reviews.stream()
                .map(reviewMapper::fromReviewToReviewDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/can-review/{freelancerId}")
    @Transactional(readOnly = true)
    public ResponseEntity<CanReviewDTO> canReviewFreelancer(@PathVariable Long freelancerId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            Recruiter recruiter = recruiterService.getRecruiterByEmail(email);
            if (recruiter == null) {
                return ResponseEntity.ok(CanReviewDTO.builder()
                        .canReview(false)
                        .hasReviewed(false)
                        .hasWorkedTogether(false)
                        .existingReviewId(null)
                        .build());
            }

            boolean hasWorkedTogether = reviewService.canRecruiterReviewFreelancer(recruiter.getId(), freelancerId);
            boolean hasReviewed = reviewService.hasRecruiterReviewedFreelancer(recruiter.getId(), freelancerId);
            boolean canReview = hasWorkedTogether && !hasReviewed;
            
            Long existingReviewId = null;
            if (hasReviewed) {
                existingReviewId = reviewService.getRecruiterReviewForFreelancer(recruiter.getId(), freelancerId)
                        .map(Review::getId)
                        .orElse(null);
            }

            return ResponseEntity.ok(CanReviewDTO.builder()
                    .canReview(canReview)
                    .hasReviewed(hasReviewed)
                    .hasWorkedTogether(hasWorkedTogether)
                    .existingReviewId(existingReviewId)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(CanReviewDTO.builder()
                    .canReview(false)
                    .hasReviewed(false)
                    .hasWorkedTogether(false)
                    .existingReviewId(null)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            Recruiter recruiter = recruiterService.getRecruiterByEmail(email);
            if (recruiter == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Unauthorized"));
            }

            Review review = reviewService.getReviewById(id)
                    .orElseThrow(() -> new RuntimeException("Review not found"));

            if (review.getRecruiter() == null || !review.getRecruiter().getId().equals(recruiter.getId())) {
                return ResponseEntity.badRequest().body(Map.of("error", "You can only delete your own reviews"));
            }

            reviewService.deleteReview(id);
            return ResponseEntity.ok(Map.of("message", "Review deleted successfully"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody CreateReviewDTO request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            Recruiter recruiter = recruiterService.getRecruiterByEmail(email);
            if (recruiter == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Unauthorized"));
            }

            if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Rating must be between 1 and 5"));
            }

            Review review = reviewService.updateReview(id, recruiter.getId(), request.getRating(), request.getComment());

            Map<String, Object> response = new HashMap<>();
            response.put("id", review.getId());
            response.put("message", "Review updated successfully");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
