package unlockway.unlockway_api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.models.UserModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetNotificationsDTO {
    private UUID id;
    private String title;
    private String description;
    private boolean read;
    private LocalDateTime date;
}
