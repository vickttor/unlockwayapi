package unlockway.unlockway_api.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import unlockway.unlockway_api.DTO.SendNotificationPayloadDTO;
import unlockway.unlockway_api.firebase.FirebaseCloudMessagingService;
import unlockway.unlockway_api.models.relationships.RoutineMealModel;
import unlockway.unlockway_api.services.impl.NotificationService;
import unlockway.unlockway_api.services.MealsRoutineOfTheDay;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSchedulerService {

    private final MealsRoutineOfTheDay mealsRoutineOfTheDayService;
    private final FirebaseCloudMessagingService firebaseCloudMessagingService;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * ?") // Run every minute;
    public void sendScheduledNotifications() {
        // Get current time in UTC
        Instant utcNow = Instant.now();

        // Convert to Brazil time zone
        ZoneId brazilTimeZone = ZoneId.of("America/Sao_Paulo");
        
        // Get the current time
        LocalTime currentBrazilTime = LocalTime.ofInstant(utcNow, brazilTimeZone);

        List<RoutineMealModel> mealRoutines = mealsRoutineOfTheDayService.getActualMealRoutines();

        for (RoutineMealModel meal : mealRoutines) {

            if (Duration.between(currentBrazilTime, meal.getNotifyAt()).toMinutes() < 1) {
                SendNotificationPayloadDTO notification = SendNotificationPayloadDTO.builder()
                    .title(meal.getMeal().getName())
                    .body(meal.getMeal().getDescription())
                    .image(meal.getMeal().getPhoto())
                    .build();

                firebaseCloudMessagingService.sendNotification(meal.getMeal().getUserModel().getDeviceToken(), notification);
                notificationService.saveNotification(meal.getMeal().getUserModel(), notification);

                mealsRoutineOfTheDayService.removeMeal(meal.getId()); // Removing from the list after notify
            }
        }
    }
}
