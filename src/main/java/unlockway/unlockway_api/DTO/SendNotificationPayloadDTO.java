package unlockway.unlockway_api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class SendNotificationPayloadDTO {
    private String title;
    private String body;
    private String image;
}
