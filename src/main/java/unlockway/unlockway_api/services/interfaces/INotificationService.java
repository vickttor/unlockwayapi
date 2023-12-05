package unlockway.unlockway_api.services.interfaces;

import unlockway.unlockway_api.DTO.GetNotificationsDTO;
import unlockway.unlockway_api.DTO.SendNotificationPayloadDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.NotificationModel;
import unlockway.unlockway_api.models.UserModel;

import java.util.List;
import java.util.UUID;

public interface INotificationService {

    List<GetNotificationsDTO> findAllNotificationsByUser(UUID userId);
    void saveNotification(UserModel user, SendNotificationPayloadDTO notification);
    void markNotificationAsRead(UUID id) throws ResourceNotFoundException;
}
