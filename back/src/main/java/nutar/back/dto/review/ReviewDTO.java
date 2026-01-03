package nutar.back.dto.review;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private String authorName;
    private LocalDateTime createdAt;
    private Long freelancerId;
    private Long recruiterId;
    private Long missionId;
    private String recruiterName;
    private String missionTitle;
}
