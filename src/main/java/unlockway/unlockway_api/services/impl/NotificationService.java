package unlockway.unlockway_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import unlockway.unlockway_api.DTO.GetNotificationsDTO;
import unlockway.unlockway_api.DTO.SendNotificationPayloadDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.NotificationModel;
import unlockway.unlockway_api.models.UserModel;
import unlockway.unlockway_api.repositories.NotificationRepository;
import unlockway.unlockway_api.services.interfaces.INotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository repository;

    @Override
    public List<GetNotificationsDTO> findAllNotificationsByUser(UUID userId) {
        List<NotificationModel> notifications = repository.findByUserId(userId);

        return notifications.stream().map((n)->{
            return GetNotificationsDTO.builder()
                .id(n.getId())
                .title(n.getTitle())
                .description(n.getDescription())
                .read(n.isRead())
                .date(n.getDate())
                .build();
        }).toList();
    }

    @Override
    public void saveNotification(UserModel user, SendNotificationPayloadDTO notificationDTO) {
        NotificationModel notification = NotificationModel.builder()
                .title(notificationDTO.getTitle())
                .description(notificationDTO.getBody())
                .read(false)
                .date(LocalDateTime.now())
                .userModel(user)
                .build();

        repository.save(notification);
    }

    @Override
    public void markNotificationAsRead(UUID id) throws ResourceNotFoundException {
        var notification = repository.findById(id).orElseThrow(()->new  ResourceNotFoundException("Notificação não existe"));
        notification.setRead(true);
        repository.save(notification);
    }

}
