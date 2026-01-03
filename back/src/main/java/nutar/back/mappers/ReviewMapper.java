package nutar.back.mappers;

import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Recruiter;
import nutar.back.dao.entites.Review;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.dao.repositories.RecruiterRepository;
import nutar.back.dto.review.CreateReviewDTO;
import nutar.back.dto.review.ReviewDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ModelMapper modelMapper;
    private final FreelancerRepository freelancerRepository;
    private final RecruiterRepository recruiterRepository;

    public ReviewMapper(ModelMapper modelMapper, 
                       FreelancerRepository freelancerRepository,
                       RecruiterRepository recruiterRepository) {
        this.modelMapper = modelMapper;
        this.freelancerRepository = freelancerRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public Review fromCreateReviewDTOToReview(CreateReviewDTO dto, Long recruiterId) {
        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        
        Freelancer freelancer = freelancerRepository.findById(dto.getFreelancerId()).orElse(null);
        review.setFreelancer(freelancer);
        
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElse(null);
        review.setRecruiter(recruiter);
        
        if (recruiter != null) {
            review.setAuthorName(recruiter.getFirstName() + " " + recruiter.getLastName());
        }
        
        return review;
    }

    public ReviewDTO fromReviewToReviewDTO(Review review) {
        ReviewDTO dto = ReviewDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .authorName(review.getAuthorName())
                .createdAt(review.getCreatedAt())
                .build();

        if (review.getFreelancer() != null) {
            dto.setFreelancerId(review.getFreelancer().getId());
        }

        if (review.getRecruiter() != null) {
            dto.setRecruiterId(review.getRecruiter().getId());
            dto.setRecruiterName(review.getRecruiter().getFirstName() + " " + review.getRecruiter().getLastName());
        }

        if (review.getMission() != null) {
            dto.setMissionId(review.getMission().getId());
            dto.setMissionTitle(review.getMission().getTitle());
        }

        return dto;
    }
}
