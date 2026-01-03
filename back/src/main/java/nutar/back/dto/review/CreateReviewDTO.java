package nutar.back.dto.review;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateReviewDTO {
    private Long freelancerId;
    private Integer rating;
    private String comment;
}
