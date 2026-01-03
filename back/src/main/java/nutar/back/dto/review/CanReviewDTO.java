package nutar.back.dto.review;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CanReviewDTO {
    private Boolean canReview;
    private Boolean hasReviewed;
    private Boolean hasWorkedTogether;
    private Long existingReviewId; 
}
